#!/usr/bin/env bash

mvn clean install

current_dir=`pwd`
artefactId="$1"
targetdir="tmp"

cd $targetdir
rm -rf $artefactId

mvn archetype:generate                                  \
  -DarchetypeGroupId=com.github.iarellano               \
  -DarchetypeArtifactId=single-apiproxy-archetype       \
  -DarchetypeVersion=1.0-SNAPSHOT                       \
  -DgroupId=com.deploy.test                             \
  -DartifactId=$artefactId                             \
  -DtestOrganization=asantosdevportal-eval              \
  -DinteractiveMode=false

cd $current_dir