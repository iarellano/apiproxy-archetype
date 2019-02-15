#!/usr/bin/env bash

mvn clean install

current_dir=`pwd`
archetypeId="$1"
targetdir="tmp"

cd $targetdir
rm -rf $archetypeId

mvn archetype:generate                                  \
  -DarchetypeGroupId=com.github.iarellano               \
  -DarchetypeArtifactId=single-apiproxy-archetype       \
  -DarchetypeVersion=1.0-SNAPSHOT                       \
  -DgroupId=com.deploy.test                             \
  -DartifactId=$archetypeId                             \
  -DinteractiveMode=false

cd $current_dir