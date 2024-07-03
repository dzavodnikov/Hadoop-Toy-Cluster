#!/usr/bin/env bash

AZKABAN_VER=4.0.0
AZKABAN_DISTR=azkaban-${AZKABAN_VER}
AZKABAN_ARCH=${AZKABAN_DISTR}.tar.gz

IMAGE=azkaban-build
CONTAINER=azkaban-build

docker build --tag ${IMAGE} .

docker run --name ${CONTAINER} --detach ${IMAGE}
docker cp ${CONTAINER}:${AZKABAN_ARCH} ..

docker stop ${CONTAINER}
docker rm   ${CONTAINER}
