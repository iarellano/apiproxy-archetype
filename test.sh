#!/usr/bin/env bash

mvn clean install

current_dir=`pwd`
archetypeId="archetype-artifact-test"
archetypeId="template_apiproxy_v1"
targetdir="tmp"

cd $targetdir
rm -rf $archetypeId

mvn archetype:generate                                  \
  -DarchetypeGroupId=com.github.iarellano               \
  -DarchetypeArtifactId=single-apiproxy-archetype       \
  -DarchetypeVersion=1.0-SNAPSHOT                       \
  -DgroupId=com.banorte.iarellano                       \
  -DartifactId=$archetypeId \
  -DinteractiveMode=false

cd $archetypeId
mvn -P test clean test

cd $current_dir