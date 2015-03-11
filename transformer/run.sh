#!/bin/bash

# Variables
ZIP_FILE_NAME=full_greater_sydney_gtfs_static.zip

ZIP_URL=https://tdx.transportnsw.info/download/files/$ZIP_FILE_NAME

GTFS_FOLDER=gtfs

OUTPUT_FOLDER=output

APP_EXECUTABLE=app/bin/app

if [ "$TDX_USERNAME" == "" ]; then
    echo "TDX_USERNAME not set"
    exit
fi

if [ "$TDX_PASSWORD" == "" ]; then
    echo "TDX_PASSWORD not set"
    exit
fi

# Download the gtfs zip
echo "Downloading zip..."
wget --http-user=$TDX_USERNAME --http-password=$TDX_PASSWORD $ZIP_URL

# Unzip to directory
echo "Unzipping folder..."
unzip $ZIP_FILE_NAME -d $GTFS_FOLDER

mkdir -p $OUTPUT_FOLDER

echo "Executing app..."
$APP_EXECUTABLE $GTFS_FOLDER $OUTPUT_FOLDER

echo "Removing working files"
rm $OUTPUT_FOLDER/*.db

echo "Done!"


