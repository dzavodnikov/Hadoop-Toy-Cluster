#!/usr/bin/env bash

STATUSES=(
    200
    302
)

function check_ports() {
    for port in "${@}"
    do
        res=$(curl --output /dev/null --silent --write-out "%{http_code}" $(hostname):${port})

        if [[ ! ${STATUSES[@]} =~ ${res} ]]
        then
            echo "Port ${port} is not available"
            exit 1
        fi
    done
}
