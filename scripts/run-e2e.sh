#!/usr/bin/env bash
set -e

source "$(dirname "$0")/.logging.sh"
source "$(dirname "$0")/.env.sh"

APK_PATH="app/build/outputs/apk/debug/app-debug.apk"

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
android emulator start "$AVD_NAME"
log_success "Emulator ready."

# 3. Test Cycle: Empty State
log_step "Preparing 'Empty State' environment..."
adb emu avd snapshot load clean_state
sleep 2 # Stabilization
log_info "Installing Application..."
adb install -r "$APK_PATH"
log_step "Running E2E: Empty State..."
maestro test e2e/empty_launcher_flow.yaml
log_success "Empty state tests passed."

# 4. Test Cycle: Populated State
log_step "Preparing 'Populated State' environment..."
adb emu avd snapshot load populated_state
sleep 2 # Stabilization
log_info "Installing Application..."
adb install -r "$APK_PATH"
log_step "Running E2E: Populated State..."
maestro test e2e/populated_launcher_flow.yaml
log_success "Populated state tests passed."

log_success "All E2E Tests Completed Successfully!"
log_banner
