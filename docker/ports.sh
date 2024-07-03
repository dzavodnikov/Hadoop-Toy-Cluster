#!/usr/bin/env bash

SLEEP_SEC=10
HOSTNAME=$(hostname -f)

function check() {
    for port in "$@"; do
        curl --silent --fail "http://${HOSTNAME}:${port}/" > /dev/null
        local result=$?

        if [ "${result}" -ne 0 ]; then
            echo "Port ${port} is closed"
            return 1
        fi
    done

    return 0
}

function wait() {
    echo "Waiting for ports $*"

    local result=1
    until [ "${result}" -eq 0 ]; do
        check "$@"
        local result=$?

        if [ "${result}" -ne 0 ]; then
            echo "Waiting ${SLEEP_SEC} seconds until the repeat..."

            sleep "${SLEEP_SEC}"
        fi
    done

    return 0
}

if [ $# -lt 2 ]; then
    echo "Usage:"
    echo "    ${0} check|wait PORT [... PORT]"

    exit 1
fi

"${1}" "${@:2}"
