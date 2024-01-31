@echo off
set DOCKER_CONTAINER_NAME=filegdbwebbuilder
set DOCKER_IMAGE_NAME=filegdbwebbuilder/filegdbwebbuilder:0.0.1-SNAPSHOT
set PORT=8080:8080

docker run -p%PORT% --name %DOCKER_CONTAINER_NAME% %DOCKER_IMAGE_NAME%
