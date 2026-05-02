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

# 0. Resolve and Validate Flows
FLOWS_ARGS=("$@")
if [ ${#FLOWS_ARGS[@]} -eq 0 ]; then
    RESOLVED_FLOWS=(e2e/*.yaml)
else
    RESOLVED_FLOWS=()
    for arg in "${FLOWS_ARGS[@]}"; do
        flow="$arg"
        if [ ! -e "$flow" ]; then
            if [ -e "e2e/$arg" ]; then
                flow="e2e/$arg"
            elif [ -e "e2e/$arg.yaml" ]; then
                flow="e2e/$arg.yaml"
            fi
        fi

        if [ -e "$flow" ]; then
            RESOLVED_FLOWS+=("$flow")
        else
            log_error "Flow file not found: $arg"
            exit 1
        fi
    done
fi

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

# 3. Running E2E Flows
for flow in "${RESOLVED_FLOWS[@]}"; do
    flow_name=$(basename "$flow")
    
    # Determine snapshot based on name
    snapshot="populated_state"
    if [[ "$flow_name" == *"empty"* ]]; then
        snapshot="clean_state"
    fi

    log_step "Preparing '$snapshot' environment for $flow_name..."
    adb emu avd snapshot load "$snapshot"
    sleep 2 # Stabilization

    log_info "Installing Application..."
    adb install -r "$APK_PATH"

    log_banner "Running E2E Flow: $flow_name"
    maestro test "$flow"
    log_success "Flow $flow_name passed."
done

log_success "All E2E Tests Completed Successfully!"
log_banner
