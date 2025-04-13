#!/bin/bash

set -ex

cd ../

docker compose up -d

./gradlew build

./gradlew check

./gradlew integrationTest