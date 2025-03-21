#!/usr/bin/env bash

function spark() {
    local CLASS_NAME=${1}
    local JOB_NAME=${2}

    local JAR=/cluster-examples-1.0.0-SNAPSHOT/lib/cluster-examples-1.0.0-SNAPSHOT.jar

    spark-submit \
        --master yarn \
        --conf spark.driver.extraClassPath="${JAR}" \
        --conf spark.executor.extraClassPath="${JAR}" \
        --class "${CLASS_NAME}" \
        --name "${JOB_NAME}" \
        "${JAR}" \
            "${@:3}"
}

spark pro.zavodnikov.spark.Pi SparkPiTest
