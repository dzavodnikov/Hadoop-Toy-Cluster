#!/usr/bin/env bash

CONTAINER=cup-master1

docker exec -it "${CONTAINER}" hdfs dfs -ls /
