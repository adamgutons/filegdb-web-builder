FROM ubuntu:22.04

ARG JAR_FILE=build/libs/*.jar

ENV LD_LIBRARY_PATH=/gdal_install/lib:/gdal_install/share/java

RUN apt-get update -y \
    && apt-get install -y openjdk-17-jdk \
    && apt-get install -y libproj-dev \
    && rm -rf /var/lib/apt/lists/*

RUN ldconfig

COPY /gdal_install /gdal_install
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
