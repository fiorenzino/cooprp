FROM registry.access.redhat.com/jboss-eap-7/eap71-openshift

ADD  modules /opt/eap/modules
ADD  configuration/standalone-openshift.xml /opt/eap/standalone/configuration/standalone-openshift.xml
ADD  target/cooprp.war /opt/eap/standalone/deployments/cooprp.war

ENV DEBUG true
ENV TZ Europe/Rome

EXPOSE 8787



