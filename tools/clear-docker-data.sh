#!/bin/sh
cd ../tk-classified-ads-platform/ad-platform/
docker compose down
rm -f ad-image-data/*.*
rm -f avatar-image-data/*.*
rm -f postgres-data/*
rm -f redis-data/*
