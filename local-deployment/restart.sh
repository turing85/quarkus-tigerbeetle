#!/usr/bin/env bash

docker-compose --file docker-compose.yml down
docker-compose --file docker-compose.yml up --detach
