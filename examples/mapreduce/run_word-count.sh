#!/usr/bin/env bash

cd /project \
&& mvn clean install -DskipTests \
&& ./deploy.sh pro.zavodnikov cluster-examples 1.0.0-SNAPSHOT

APP_DIR=/cluster-examples-1.0.0-SNAPSHOT

java -cp "${APP_DIR}:${APP_DIR}/lib/*:$(hadoop classpath):" \
    -Dlog4j.configuration="file:${APP_DIR}/log4j.properties" \
    pro.zavodnikov.mapreduce.WordCount \
        --input  /word-count \
        --output /word-count_result \
> /var/log/word-count.log 2>&1
