#!/usr/bin/env bash
set -e

source "$(dirname "$0")/.logging.sh"
source "$(dirname "$0")/.env.sh"

AVD_NAME="medium_phone" # target emulator name

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


log_step "Checking for AVD '$AVD_NAME'..."
if ! android emulator list | grep -q "$AVD_NAME"; then
    log_info "Creating new AVD from profile '$AVD_NAME'..."
    android emulator create --profile="$AVD_NAME"
    log_success "AVD '$AVD_NAME' created successfully."
else
    log_info "AVD '$AVD_NAME' already exists. Skipping creation."
fi
log_footer

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
