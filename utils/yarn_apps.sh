#!/usr/bin/env bash

CONTAINER=cup-master1

docker exec -it "${CONTAINER}" yarn application -list

echo
echo "To kill the app use:"
echo "    \$ docker exec -it ${CONTAINER} yarn application -kill <APP_ID>"
