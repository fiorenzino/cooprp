#!/usr/bin/env bash
cd docker/
docker-compose stop
cd ..
mvn -Dmaven.test.skip=true clean package
docker build -t eap71-cooprp -f docker/Dockerfile .
cd docker/
docker-compose up