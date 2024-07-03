#!/usr/bin/env bash

source .env

echo "Stop Hadoop cluster ${CLUSTER_VERSION} and clean data/log directories..."

docker compose down

rm -rf "data/${CLUSTER_VERSION}/"
rm -rf "logs/${CLUSTER_VERSION}/"
