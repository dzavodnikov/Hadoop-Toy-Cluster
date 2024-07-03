#!/usr/bin/env bash

source .env

echo "Start Hadoop cluster ${CLUSTER_VERSION}..."

docker compose up --detach --wait
