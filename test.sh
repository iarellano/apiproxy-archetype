#!/usr/bin/env bash

rm -rf ~/.m2/repository/org/apache/maven
rm ~/.m2/repository/archetype-catalog.xml

mvn clean install

current_dir=`pwd`
artefactId="$1"
targetdir="tmp"

cd $targetdir
rm -rf *
cp ../petstore.yaml ./
cp ../petstore.json ./

mvn archetype:generate                                  \
  -DarchetypeGroupId=com.github.iarellano               \
  -DarchetypeArtifactId=openapi2apigee-archetype        \
  -DarchetypeVersion=1.0.0-SNAPSHOT                     \
  -DgroupId=com.deploy.test                             \
  -DartifactId=$artefactId                              \
  -DtestOrganization=asantosdevportal-eval              \
  -DinteractiveMode=true                                \
  -Dspec=./petstore.json
#  -Dspec=https://raw.githubusercontent.com/OAI/OpenAPI-Specification/master/examples/v3.0/petstore.yaml
#  -Dspec=https://raw.githubusercontent.com/OAI/OpenAPI-Specification/master/examples/v2.0/yaml/petstore.yaml
#  -Dspec=https://raw.githubusercontent.com/OAI/OpenAPI-Specification/master/examples/v2.0/yaml/petstore.yaml

