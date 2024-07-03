#!/usr/bin/env bash

source VARS
source ../env.sh

./wc_input.sh

hadoop_run pro.zavodnikov.mapreduce.WordCount \
    --input  /word_count \
    --output /word_count_result

./wc_output.sh
