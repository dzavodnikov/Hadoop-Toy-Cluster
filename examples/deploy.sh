#!/usr/bin/env bash

source VARS
source ../env.sh

# Build.
mvn -f .. clean install -DskipTests

deploy
