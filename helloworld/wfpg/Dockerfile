# Copy of most of https://registry.hub.docker.com/u/jboss/wildfly/dockerfile/
# to use JDK 8
# Use latest jboss/base-jdk:8 image as the base
FROM jboss/base-jdk:8

# Set the WILDFLY_VERSION env variable
ENV WILDFLY_VERSION 10.1.0.Final
ENV PG_JDBC_VERSION 42.1.4

# $HOME appears to not be set
ENV HOME /opt/jboss/

# Add the WildFly distribution to /opt, and make wildfly the owner of the extracted tar content
# Make sure the distribution is available from a well-known place
RUN cd $HOME && curl http://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz | tar zx && mv $HOME/wildfly-$WILDFLY_VERSION $HOME/wildfly

# Set the JBOSS_HOME env variable
ENV JBOSS_HOME /opt/jboss/wildfly

# Expose the ports we're interested in
EXPOSE 8080 9990

# Get Postgres stuff
RUN curl -o /tmp/postgresql-jdbc.jar https://jdbc.postgresql.org/download/postgresql-${PG_JDBC_VERSION}.jar
ADD config.sh /tmp/
ADD batch.cli /tmp/

# Set up modules
RUN /tmp/config.sh
##RUN $JBOSS_HOME/bin/add-user.sh admin Admin#70365 --silent

# Use the modules
##ADD standalone.xml /opt/jboss/wildfly/standalone/configuration/

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
