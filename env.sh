#!/usr/bin/env bash

function required_var() {
    if [[ -z "${!1}" ]]
    then
        echo "Variable '${1}' should be defined"
        exit 1
    fi
}

required_var APP_GROUP
required_var APP_NAME
required_var APP_VERSION

MASTER1=toy-master1
WORKER1=toy-worker1
WORKER2=toy-worker2
WORKER3=toy-worker3

ZKQ=zookeeper1.toy
APP_DIR=/apps/${APP_NAME}
LOG_DIR=/var/log/apps

function in_container() {
    local CONTAINER=${1}
    local MISC=${*:2}

    docker exec -it "${CONTAINER}" bash -c "${MISC}"
}

function in_container_bg() {
    local CONTAINER=${1}
    local MISC=${*:2}

    docker exec -d "${CONTAINER}" bash -c "${MISC}"
    echo "${MISC}"
}

function to_container() {
    local CONTAINER=${1}
    local FROM=${2}
    local TO=${3}

    docker cp "${FROM}" "${CONTAINER}:${TO}"
}

function from_container() {
    local CONTAINER=${1}
    local FROM=${2}
    local TO=${3}

    docker cp "${CONTAINER}:${FROM}" "${TO}"
}

function hdfs_run() {
    local MISC=${*:0}

    in_container "${MASTER1}" \
        "hdfs dfs ${MISC}"
}

function to_hdfs() {
    local FROM=${1}
    local TO=${2}
    local TMP="/tmp/to_hdfs_${RANDOM}"

    in_container "${MASTER1}" \
        "mkdir -p ${TMP}"
    to_container "${MASTER1}" "${FROM}" "${TMP}"
    hdfs_run -put -f "${TMP}/${FROM}" "${TO}"
    in_container "${MASTER1}" \
        "rm -rf ${TMP}"
}

function from_hdfs() {
    local FROM=${1}
    local TO=${2}
    local TMP="/tmp/from_hdfs_${RANDOM}"

    in_container "${MASTER1}" \
        "mkdir -p ${TMP}"
    hdfs_run -get "${FROM}" "${TMP}"
    # FROM is absolute path in HDFS, so no need to separate it by slash in path under TMP directory.
    from_container "${MASTER1}" "${TMP}${FROM}" "${TO}"
    in_container "${MASTER1}" \
        "rm -rf ${TMP}"
}

function deploy() {
    local APP_TGZ=${APP_NAME}-${APP_VERSION}.tar.gz

    in_container "${MASTER1}" \
        "rm -rf ${APP_DIR}"
    in_container "${MASTER1}" \
        "mkdir -p ${APP_DIR}"

    mvn dependency:copy -Dartifact="${APP_GROUP}:${APP_NAME}:${APP_VERSION}:tar.gz" -DoutputDirectory=.

    to_container "${MASTER1}" "${APP_TGZ}" "${APP_DIR}/${APP_TGZ}"
    rm "${APP_TGZ}"

    in_container "${MASTER1}" \
        "tar --extract --strip-components=1 --file=${APP_DIR}/${APP_TGZ} --directory=${APP_DIR}"
}

function hbase_run() {
    local MISC=${*:0}

    in_container "${MASTER1}" \
        "java -cp ${APP_DIR}/*:" \
        "-Dhostname=\$(hostname -f)" \
        "-Dhbase.zookeeper.quorum=${ZKQ}" \
        "${MISC}"
}

function hadoop_run() {
    local MISC=${*:0}

    in_container "${MASTER1}" \
        "java -cp \$(hadoop classpath):${APP_DIR}/*:" \
        "-Dlog4j.configuration=file:${APP_DIR}/log4j.properties" \
    "${MISC}"
}

function spark_run() {
    local NAME=${1}
    local CLASS=${2}
    local MISC=${*:3}

    CLASSPATH="${APP_DIR}/*:"
    in_container "${MASTER1}" \
        spark-submit \
        --master yarn \
        "--conf spark.driver.extraClassPath=${CLASSPATH}" \
        "--conf spark.executor.extraClassPath=${CLASSPATH}" \
        "--name ${NAME}" \
        "--class ${CLASS}" \
        "${APP_DIR}/${APP_NAME}-${APP_VERSION}.jar" \
        "${MISC}"
}
