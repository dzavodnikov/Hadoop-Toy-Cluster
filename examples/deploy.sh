#!/usr/bin/env bash

cd /project \
&& mvn clean install -DskipTests \
&& ./deploy.sh pro.zavodnikov cluster-examples 1.0.0-SNAPSHOT
