#!/bin/bash

set -ex

cd ../

./gradlew build

docker build -t hms-app:latest .

docker run -d \
  --name hms-app \
  -p 8080:8080 \
  --network=hms_hms-network \
  -d \
  hms-app:latest