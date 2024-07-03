#!/usr/bin/env bash

APP_GROUP=${1}
APP_NAME=${2}
APP_VERSION=${3}

APP_DIR="${APP_NAME}-${APP_VERSION}"
ARCH_EXT=tar.gz
APP_ARCH="${APP_DIR}.${ARCH_EXT}"

mvn dependency:copy \
    -Dartifact="${APP_GROUP}:${APP_NAME}:${APP_VERSION}:${ARCH_EXT}" \
    -DoutputDirectory=/

tar --extract --file="/${APP_ARCH}" --directory=/
