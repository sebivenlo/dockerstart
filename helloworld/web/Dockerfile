FROM wfpg
##FROM jboss/wildfly
MAINTAINER EricSoldierer

RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin#70365 --silent
ADD config.sh /tmp/
ADD batch.cli /tmp/

# Set up modules
RUN /tmp/config.sh

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b=0.0.0.0", "-bmanagement=0.0.0.0"]
