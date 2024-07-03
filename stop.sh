#!/usr/bin/env bash

source .env

echo "Stop Hadoop cluster ${CLUSTER_VERSION}..."

docker compose down
