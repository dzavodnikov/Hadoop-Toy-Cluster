#!/usr/bin/env bash

cd /project \
&& mvn clean install -DskipTests \
&& ./deploy.sh pro.zavodnikov cluster-examples 1.0.0-SNAPSHOT

JAR="/cluster-examples-1.0.0-SNAPSHOT/lib/cluster-examples-1.0.0-SNAPSHOT.jar"

spark-submit \
    --master yarn \
    --conf spark.driver.extraClassPath="${JAR}" \
    --conf spark.executor.extraClassPath="${JAR}" \
    --class pro.zavodnikov.spark.Pi \
    --name SparkPiTest \
    "${JAR}" \
> /var/log/spark-pi-test.log 2>&1
