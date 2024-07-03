#!/usr/bin/env bash

docker exec \
    --interactive --tty \
    --workdir /project \
    toy-master1 \
    bash
