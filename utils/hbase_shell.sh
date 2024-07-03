#!/usr/bin/env bash

CONTAINER=cup-master1

docker exec -it "${CONTAINER}" hbase shell
