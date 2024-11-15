#!/usr/bin/env bash

docker-compose --file docker-compose.yml down

if [[ -d tigerbeetle ]]
then
  rm -rf tigerbeetle
fi

mkdir -p tigerbeetle/node{0..2}/data

podman run --rm --security-opt seccomp=unconfined -v $(pwd)/tigerbeetle/node0/data:/data ghcr.io/tigerbeetle/tigerbeetle format --cluster=0 --replica=0 --replica-count=3 /data/conf.tigerbeetle
podman run --rm --security-opt seccomp=unconfined -v $(pwd)/tigerbeetle/node1/data:/data ghcr.io/tigerbeetle/tigerbeetle format --cluster=0 --replica=1 --replica-count=3 /data/conf.tigerbeetle
podman run --rm --security-opt seccomp=unconfined -v $(pwd)/tigerbeetle/node2/data:/data ghcr.io/tigerbeetle/tigerbeetle format --cluster=0 --replica=2 --replica-count=3 /data/conf.tigerbeetle

docker-compose --file docker-compose.yml up --detach
