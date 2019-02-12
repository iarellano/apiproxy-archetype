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
  -DgroupId=com.banorte.iarellano                       \
  -DartifactId=$archetypeId                             \
  -DtestOrganization=iarellano-eval                     \
  -DinteractiveMode=false

#cd $archetypeId/src/main/gateway/template-v1
#mvn -P test clean test
#
#mvn fr.jcgay.maven.plugins:buildplan-maven-plugin:list-plugin

cd $current_dir