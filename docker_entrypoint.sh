#!/bin/sh

service stackstate-agent start
service nginx start

cd /app
mvn install
mvn -e exec:java -Dexec.mainClass=com.force.samples.util.DataLoadUtil

export MAVEN_OPTS="-Dsts.writer.type=LoggingWriter -javaagent:/sts-java-agent-0.6.1-SNAPSHOT.jar -Dstackstate.slf4j.simpleLogger.defaultLogLevel=debug"

mvn tomcat7:run
