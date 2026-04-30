#!/usr/bin/env bash
set -e

source "$(dirname "$0")/.logging.sh"
source "$(dirname "$0")/.env.sh"

SNAPSHOT="${1:-clean_state}"
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"

log_banner "Manual Test Session: $SNAPSHOT"

if [ ! -f "$APK_PATH" ]; then
    log_error "APK not found at $APK_PATH. Run 'mise run build' first."
    exit 1
fi

log_step "Starting Emulator ($AVD_NAME)..."
android emulator start "$AVD_NAME"

log_step "Loading snapshot: $SNAPSHOT..."
# Use adb emu avd snapshot load to specifically target the requested snapshot
if adb emu avd snapshot load "$SNAPSHOT" | grep -q "OK"; then
    log_success "Snapshot '$SNAPSHOT' loaded successfully."
else
    # Fallback/Error handling if snapshot doesn't exist
    log_error "Failed to load snapshot '$SNAPSHOT'. Does it exist?"
    exit 1
fi

sleep 2 # Stabilization

log_step "Installing latest Application..."
adb install -r "$APK_PATH"

log_success "Manual test environment ready!"
log_info "Snapshot: $SNAPSHOT"
log_info "App: $APK_PATH"
log_footer
