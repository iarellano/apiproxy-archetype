mvn dependency:get -Dartifact=com.apigee:com.apigee.expressions:1.0.0 -o
if errorLevel 1 mvn install:install-file -Dfile=lib/expressions-1.0.0.jar  -DgroupId=com.apigee -DartifactId=com.apigee.expressions -Dversion=1.0.0 -Dpackaging=jar

mvn dependency:get -Dartifact=com.apigee:com.apigee.flow:1.0.0 -o
if errorLevel 1 mvn install:install-file -Dfile=lib/com.apigee.flow-1.0.0.jar  -DgroupId=com.apigee -DartifactId=com.apigee.flow -Dversion=1.0.0 -Dpackaging=jar