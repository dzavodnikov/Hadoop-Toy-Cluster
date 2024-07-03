#!/usr/bin/env bash

echo "Stop Toy cluster and clean data/log directories..."

docker compose down

rm -rf data/
rm -rf logs/
