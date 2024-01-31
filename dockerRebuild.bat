@echo off
set DOCKER_CONTAINER_NAME=filegdbwebbuilder
set DOCKER_IMAGE_NAME=filegdbwebbuilder/filegdbwebbuilder:0.0.1-SNAPSHOT

docker stop %DOCKER_CONTAINER_NAME%
docker rm %DOCKER_CONTAINER_NAME%

docker image rm %DOCKER_IMAGE_NAME%

call gradlew build

docker build . -t %DOCKER_IMAGE_NAME%
