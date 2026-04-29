#!/usr/bin/env bash

# Color definitions
BLUE='\033[0;34m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Marker generator
log_banner() {
    local title=$1
    echo -e "${BLUE}==================================================${NC}"
    if [ -n "$title" ]; then
        echo -e "${BLUE}       $title   ${NC}"
        echo -e "${BLUE}==================================================${NC}"
    fi
}

log_footer() { log_banner; printf '\n\n'; }

# Logging helpers
log_step() { echo -e "${BLUE}==>${NC} $1"; }
log_info() { echo -e "    $1"; }
log_success() { echo -e "${GREEN}SUCCESS:${NC} $1"; }
log_warn() { echo -e "${YELLOW}WARNING:${NC} $1"; }
log_error() { echo -e "${RED}ERROR:${NC} $1"; }
