#!/usr/bin/env bash

source healthcheck.sh

check_ports 8042    # YARN NodeManager
check_ports 16030   # HBase Web UI

exit 0
