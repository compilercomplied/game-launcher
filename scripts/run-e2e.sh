#!/usr/bin/env bash
set -e

source "$(dirname "$0")/.logging.sh"
source "$(dirname "$0")/.env.sh"

AVD_NAME="medium_phone"
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
FIXTURES_DIR="e2e/fixtures"

# Ensure cleanup on exit (success or failure)
cleanup() {
    log_warn "Tearing Down Emulator"
    android emulator stop "$AVD_NAME" || true
    log_info "Waiting for emulator process to exit..."
    sleep 3
}
trap cleanup EXIT

log_banner "Running End-to-End Tests (Maestro)"

# 1. Build Application
log_step "Building Application..."
./gradlew assembleDebug
if [ ! -f "$APK_PATH" ]; then
    log_error "APK not found at $APK_PATH"
    exit 1
fi
log_success "Build completed."

# 2. Start Emulator
log_step "Starting Emulator ($AVD_NAME)..."
log_info "This command will return when the emulator is fully ready."
android emulator start "$AVD_NAME"
log_success "Emulator process started."

log_step "Waiting for system and Package Manager to be ready..."
MAX_ATTEMPTS=60
ATTEMPT=1
until [ "$(adb shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = "1" ] && adb shell pm path android >/dev/null 2>&1; do
    if [ $ATTEMPT -eq $MAX_ATTEMPTS ]; then
        log_error "Timeout waiting for emulator to be ready."
        exit 1
    fi
    log_info "Waiting for system to stabilize (attempt $ATTEMPT/$MAX_ATTEMPTS)..."
    sleep 3
    ((ATTEMPT++))
done
# Small extra buffer for internal services to finish starting
sleep 5
log_success "System is ready for installation."

# 3. Install Target Application
log_step "Installing Target Application..."
adb install -r "$APK_PATH"
log_success "Application installed."

# 4. Run E2E: Empty State
log_step "Running E2E: Empty State..."
maestro test e2e/empty_launcher_flow.yaml
log_success "Empty state tests passed."

# 5. Prepare Populated State
log_step "Preparing Populated State..."
if [ -d "$FIXTURES_DIR" ] && [ "$(ls -A $FIXTURES_DIR/*.apk 2>/dev/null)" ]; then
    for apk in "$FIXTURES_DIR"/*.apk; do
        log_info "Installing Dummy Fixture: $(basename "$apk")"
        adb install -r "$apk"
    done
    log_success "Fixtures installed."
else
    log_warn "No dummy APKs found in $FIXTURES_DIR. Skipping population."
fi

# 6. Run E2E: Populated State
log_step "Running E2E: Populated State..."
maestro test e2e/populated_launcher_flow.yaml
log_success "Populated state tests passed."

log_success "All E2E Tests Completed Successfully!"
log_banner
