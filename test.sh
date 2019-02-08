#!/usr/bin/env bash

mvn clean install

current_dir=`pwd`
archetypeId="archetype-artifact-test"
archetypeId="test"
targetdir="tmp"

rm -rf $targetdir
mkdir $targetdir
cd $targetdir




mvn archetype:generate                                  \
  -DarchetypeGroupId=com.github.iarellano               \
  -DarchetypeArtifactId=single-apiproxy-archetype       \
  -DarchetypeVersion=1.0-SNAPSHOT                       \
  -DgroupId=com.banorte.iarellano                       \
  -DartifactId=$archetypeId                             \
  -DinteractiveMode=false

cd $current_dir