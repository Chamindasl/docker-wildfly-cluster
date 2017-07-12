FROM jboss/wildfly:latest

MAINTAINER Chaminda Amarasinghe

ADD standalone-full-ha-custom.xml /opt/jboss/wildfly/standalone/configuration/standalone-full-ha-custom.xml
ADD wildfly-cluster/wildfly-cluster-ear/target/wildfly-cluster-ear-1.0-SNAPSHOT.ear /opt/jboss/wildfly/standalone/deployments/wildfly-cluster-ear-1.0-SNAPSHOT.ear

# Change the user to root for changing the ownership of our files
USER root
RUN chown jboss:jboss /opt/jboss/wildfly/standalone/configuration/standalone-full-ha-custom.xml
RUN chown jboss:jboss /opt/jboss/wildfly/standalone/deployments/wildfly-cluster-ear-1.0-SNAPSHOT.ear
USER jboss

