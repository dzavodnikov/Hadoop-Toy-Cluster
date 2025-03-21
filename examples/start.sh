#!/usr/bin/env bash

# 1. Deploy.
cd /project/examples \
&& ./deploy.sh

# 2. Run HBase examples.
cd /project/examples \
&& ./hbase.sh

# 3. Run MapReduce example.
cd /project/examples/mapreduce \
&& ./upload_input.sh \
&& ./run_word-count.sh \
&& ./download_output.sh

# 4. Run Spark example.
cd /project/examples \
&& ./spark.sh
