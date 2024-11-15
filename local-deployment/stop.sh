#!/usr/bin/env bash

docker-compose --file docker-compose.yml down

if [[ -d tigerbeetle ]]
then
  rm -rf tigerbeetle
fi
