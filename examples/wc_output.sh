#!/usr/bin/env bash

source VARS
source ../env.sh

rm -rf     word_count_result
from_hdfs /word_count_result .
