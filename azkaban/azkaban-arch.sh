#!/usr/bin/env bash

rm ./*.zip 2> /dev/null

zip -r azkaban-jobs ./* -x ./*.sh
