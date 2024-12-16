#!/usr/bin/env sh

APP=$1
APP_PATH=$2

mkdir -p inputs/$APP/source_code

cp -r $APP_PATH/* inputs/$APP/source_code/

mkdir -p outputs/$APP/raw_results/

docker docker run -v ./inputs/:/data/ -v ./outputs/$APP/raw_results/:/outputs/ -it --rm dcalsel/topicdecomp:microrefact $APP

mkdir -p outputs/$APP/parsed_microRefact/
python parse_app.py $APP
