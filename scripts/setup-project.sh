#!/usr/bin/env bash
set -e

source "$(dirname "$0")/.logging.sh"
source "$(dirname "$0")/.env.sh"

log_banner "Initializing Graddle wrapper"
log_step "Ensuring Gradle wrapper exists..."
./gradlew wrapper
./gradlew --version
log_success "Gradle wrapper ready."
log_footer

log_banner "Installing SDK components via Android CLI..."
log_step "Targeting: platforms/android-34, build-tools/34.0.0, platform-tools, emulator"
android sdk install "platforms/android-34" "build-tools/34.0.0" "platform-tools" "emulator"
log_success "SDK components installed."

log_step "Detecting host architecture..."
ARCH=$(uname -m)
if [ "$ARCH" = "arm64" ] || [ "$ARCH" = "aarch64" ]; then
    SYS_IMAGE="system-images/android-34/google_apis/arm64-v8a"
    log_info "Detected ARM architecture ($ARCH)."
else
    SYS_IMAGE="system-images/android-34/google_apis/x86_64"
    log_info "Detected x86 architecture ($ARCH)."
fi

log_step "Installing System Image: $SYS_IMAGE..."
android sdk install "$SYS_IMAGE"
log_success "System image ready."


# Check if snapshots already exist to ensure idempotency
SNAPSHOTS_DIR="$HOME/.android/avd/${AVD_NAME}.avd/snapshots"
if [ -d "$SNAPSHOTS_DIR/clean_state" ] && [ -d "$SNAPSHOTS_DIR/populated_state" ]; then
    log_info "Snapshots 'clean_state' and 'populated_state' already exist. Skipping provisioning."
    log_info "To force re-provisioning, remove the AVD: android emulator remove --force $AVD_NAME"
else
    log_banner "Provisioning Emulator Snapshots"
    log_step "Ensuring a clean AVD '$AVD_NAME'..."
    # Stop and remove existing AVD to ensure a clean state for snapshots
    android emulator stop "$AVD_NAME" >/dev/null 2>&1 || true
    android emulator remove --force "$AVD_NAME" >/dev/null 2>&1 || true

    log_info "Creating new AVD from profile '$AVD_NAME'..."
    android emulator create --profile="$AVD_NAME"
    log_success "AVD '$AVD_NAME' created successfully."

    # Ensure snapshot consistency by forcing host GPU mode
    log_step "Configuring AVD for snapshot compatibility..."
    CONFIG_FILE="$HOME/.android/avd/${AVD_NAME}.avd/config.ini"
    if [ -f "$CONFIG_FILE" ]; then
        # Use sed to set hw.gpu.mode to host
        if grep -q "hw.gpu.mode" "$CONFIG_FILE"; then
            sed -i '' 's/hw.gpu.mode=.*/hw.gpu.mode=host/g' "$CONFIG_FILE"
        else
            echo "hw.gpu.mode=host" >> "$CONFIG_FILE"
        fi
        log_success "GPU mode set to 'host' in config.ini"
    else
        log_warn "Could not find config.ini at $CONFIG_FILE to set GPU mode."
    fi

    log_step "Starting Emulator ($AVD_NAME) for provisioning..."
    android emulator start "$AVD_NAME"

    log_step "Waiting for system to be ready..."
    MAX_ATTEMPTS=60
    ATTEMPT=1
    until [ "$(adb shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = "1" ] && adb shell pm path android >/dev/null 2>&1; do
        if [ $ATTEMPT -eq $MAX_ATTEMPTS ]; then
            log_error "Timeout waiting for emulator to be ready."
            exit 1
        fi
        sleep 3
        ((ATTEMPT++))
    done
    sleep 5 # Buffer

    log_step "Saving 'clean_state' snapshot..."
    adb emu avd snapshot save clean_state
    log_success "Clean state saved."

    log_step "Installing Fixtures..."
    if [ -d "$FIXTURES_DIR" ] && [ "$(ls -A "$FIXTURES_DIR"/*.apk 2>/dev/null)" ]; then
        for apk in "$FIXTURES_DIR"/*.apk; do
            log_info "Installing: $(basename "$apk")"
            adb install -r "$apk"
        done
        log_success "Fixtures installed."
    else
        log_warn "No dummy APKs found in $FIXTURES_DIR."
    fi

    log_step "Saving 'populated_state' snapshot..."
    adb emu avd snapshot save populated_state
    log_success "Populated state saved."

    log_step "Stopping Emulator..."
    android emulator stop "$AVD_NAME"
    log_success "Snapshots provisioned."
    log_footer
fi

log_banner "Checking Maestro CLI..."
if ! command -v maestro &> /dev/null; then
    log_info "Installing Maestro CLI..."
    curl -Ls "https://get.maestro.mobile.dev" | bash
    log_success "Maestro CLI installed."
else
    log_info "Maestro CLI is already installed."
fi
log_footer

log_success "Setup Complete!"
