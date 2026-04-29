#!/usr/bin/env bash

# This script initializes the project environment.
#
# When managing the Android SDK via 'mise', the plugin typically adds the 
# 'cmdline-tools' (which contains the 'android' and 'sdkmanager' binaries) 
# to the system PATH automatically. However, it often omits 'platform-tools', 
# which contains the 'adb' binary required for device interaction and APK installation.
#
# This script dynamically resolves the SDK location using the 'android' CLI 
# and ensures 'platform-tools' is available in the PATH for all project scripts.

SDK_PATH=$(android info sdk 2>/dev/null)
if [ -n "$SDK_PATH" ]; then
    export PATH="$PATH:$SDK_PATH/platform-tools"
fi

# Configuration
export AVD_NAME="medium_phone"
export FIXTURES_DIR="e2e/fixtures"

# Add Maestro to PATH
export PATH="$PATH:$HOME/.maestro/bin"
