#!/usr/bin/env bash

function mapreduce() {
    local CLASS_NAME=${1}

    local APP_DIR=/cluster-examples-1.0.0-SNAPSHOT

    java -cp "$(hadoop classpath):${APP_DIR}:${APP_DIR}/lib/*:" \
        -Dlog4j.configuration="file:${APP_DIR}/log4j.properties" \
        "pro.zavodnikov.mapreduce.${CLASS_NAME}" \
            "${@:2}"
}

mapreduce WordCount --input /word-count --output /word-count_result
