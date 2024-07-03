#!/usr/bin/env bash

function hbase() {
    local CLASS_NAME=${1}

    local APP_DIR=/cluster-examples-1.0.0-SNAPSHOT

    java -cp "${APP_DIR}:${APP_DIR}/lib/*:" \
        -Dlog4j.configuration="file:${APP_DIR}/log4j.properties" \
        "pro.zavodnikov.hbase.${CLASS_NAME}" \
            --zkq zookeeper1.toy:2181 \
            "${@:2}"
}

hbase CreateTable       --table tests:users --family personal
hbase ListTables
sleep 5

hbase PutRow            --table tests:users --key jsmith --family personal --column name --value John Smith
hbase GetRow            --table tests:users --key jsmith --family personal --column name
sleep 5

hbase ScanTable         --table tests:users
sleep 5

hbase AddTableFamily    --table tests:users --family tasks --max-versions 3 --ttl-seconds 10
hbase PutRow            --table tests:users --key jsmith --family tasks --column title --value Task 1
hbase PutRow            --table tests:users --key jsmith --family tasks --column title --value Task 2
hbase PutRow            --table tests:users --key jsmith --family tasks --column title --value Task 3
hbase ScanTable         --table tests:users # We are have 3 tasks.
sleep 4
hbase PutRow            --table tests:users --key jsmith --family tasks --column title --value Task 4
hbase ScanTable         --table tests:users # "Task 1" was removed by versions limit.
sleep 5
hbase ScanTable         --table tests:users # "Task 2" and "Task 3" were removed by TTL.
sleep 5

hbase ScanTable         --table tests:users # "Task 4" also was removed by TTL.
sleep 5

hbase DeleteRow         --table tests:users --key jsmith
hbase ScanTable         --table tests:users
sleep 5

hbase DropTable         --table tests:users
