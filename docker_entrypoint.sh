#!/bin/sh

service stackstate-agent start
service nginx start

cd /app
mvn install
mvn -e exec:java -Dexec.mainClass=com.force.samples.util.DataLoadUtil
mvn tomcat7:run
