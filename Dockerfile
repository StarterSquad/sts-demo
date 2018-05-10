FROM maven:3.5-jdk-8

RUN mkdir -p /app
COPY webapp-jpa-hibernate /app

COPY docker_entrypoint.sh /

RUN chmod +x /docker_entrypoint.sh

COPY agent/agent.deb /
COPY agent/sts-java-agent-0.6.1-SNAPSHOT.jar /

RUN dpkg -i /agent.deb

# add config
RUN mv /etc/sts-agent/stackstate.conf.example /etc/sts-agent/stackstate.conf \
  && sed -i -e "s/^.*log_to_syslog:.*$/log_to_syslog: no/" /etc/sts-agent/stackstate.conf \
  && sed -i '/programs=/ s/$/,trace-agent/' /etc/sts-agent/supervisor.conf

RUN apt-get -y update && apt-get -y install nginx
COPY container-app/default /etc/nginx/sites-enabled

EXPOSE 8080
EXPOSE 8081

ENTRYPOINT ["/docker_entrypoint.sh"]


