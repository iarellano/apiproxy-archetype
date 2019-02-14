#!/usr/bin/env bash

mvn install:install-file -Dfile=../lib/expressions-1.0.0.jar  -DgroupId=com.apigee -DartifactId=com.apigee.expressions -Dversion=1.0.0 -Dpackaging=jar

mvn install:install-file -Dfile=../lib/message-flow-1.0.0.jar -DgroupId=com.apigee -DartifactId=com.apigee.flow -Dversion=1.0.0 -Dpackaging=jar
