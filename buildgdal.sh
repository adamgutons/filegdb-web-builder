#!/bin/bash

BUILD_TYPE="-DCMAKE_BUILD_TYPE=Release"
JAVA_BINDINGS="-DBUILD_JAVA_BINDINGS=ON"
BUILD_OPTIONAL_DRIVERS="-DGDAL_BUILD_OPTIONAL_DRIVERS:BOOL=OFF"
ENABLE_OPENFILEGDB="-DOGR_ENABLE_DRIVER_OPENFILEGDB:BOOL=ON"
PYTHON_BINDINGS="-DBUILD_PYTHON_BINDINGS=OFF"
BUILD_APPS="-DBUILD_APPS=NO"
USE_EXTERNAL_LIBS="-DGDAL_USE_EXTERNAL_LIBS=OFF"
USE_INTERNAL_LIBS="-DGDAL_USE_INTERNAL_LIBS=ON"
INSTALL_PREFIX="-DCMAKE_INSTALL_PREFIX="
ARROW_DISABLE="-DCMAKE_DISABLE_FIND_PACKAGE_Arrow=ON"
LIB_KML_DISABLE="-DGDAL_USE_LIBKML=OFF"

gdal_binaries="gdal-3.7.3.tar.gz"  # directory of gdal binaries
gdal_install="gdal_install"  # output directory where binaries will be copied to
gdal_extract="gdal-3.7.3"  # directory where binaries should be installed

echo "$BUILD_TYPE $JAVA_BINDINGS $BUILD_OPTIONAL_DRIVERS $ENABLE_OPENFILEGDB $PYTHON_BINDINGS $BUILD_APPS \
      $ARROW_DISABLE $LIB_KML_DISABLE $USE_EXTERNAL_LIBS $USE_INTERNAL_LIBS $PROJ $INSTALL_PREFIX$gdal_install"

tar -xzf "$gdal_binaries"

mkdir -p "$gdal_install"

cd "$gdal_extract" || exit 1

mkdir -p "build"
cd "build" || exit 1

cmake $BUILD_TYPE $JAVA_BINDINGS $BUILD_OPTIONAL_DRIVERS $ENABLE_OPENFILEGDB \
      $PYTHON_BINDINGS $BUILD_APPS $ARROW_DISABLE $LIB_KML_DISABLE \
      $USE_EXTERNAL_LIBS $PROJ $PROJ_INCLUDE $USE_INTERNAL_LIBS $INSTALL_PREFIX"$gdal_install" ..

cmake --build .
cmake --build . --target install

rm -rf "$gdal_binaries" "$gdal_extract"
