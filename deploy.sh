#!/usr/bin/env bash

function check() {
    if [ -z "${1}" ]; then
        echo "${2}"
        exit 1
    fi
}

APP_GROUP=${1}
APP_NAME=${2}
APP_VERSION=${3}

check "${APP_GROUP}" "Application group was not defined"
check "${APP_NAME}" "Application name was not defined"
check "${APP_VERSION}" "Application version was not defined"

APP_DIR="${APP_NAME}-${APP_VERSION}"
ARCH_EXT=tar.gz
APP_ARCH="${APP_DIR}.${ARCH_EXT}"

rm -rf "/${APP_DIR:?}"

mvn dependency:copy \
    -Dartifact="${APP_GROUP}:${APP_NAME}:${APP_VERSION}:${ARCH_EXT}" \
    -DoutputDirectory=/

tar --extract --file="/${APP_ARCH}" --directory=/
