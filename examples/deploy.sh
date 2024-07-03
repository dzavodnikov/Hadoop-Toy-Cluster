#!/usr/bin/env bash

APP_GROUP=pro.zavodnikov
APP_NAME=cluster-examples
APP_VERSION=1.0.0-SNAPSHOT

APP_DIR="${APP_NAME}-${APP_VERSION}"
ARCH_EXT=tar.gz
APP_ARCH="${APP_DIR}.${ARCH_EXT}"

cd /project \
&& mvn clean install -DskipTests \
&& mvn dependency:copy \
    -Dartifact="${APP_GROUP}:${APP_NAME}:${APP_VERSION}:${ARCH_EXT}" \
    -DoutputDirectory=/ \
&& rm -rf "/${APP_DIR:?}" \
&& tar --extract --file="/${APP_ARCH}" --directory=/
