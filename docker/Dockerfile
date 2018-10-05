FROM registry.access.redhat.com/jboss-eap-7/eap71-openshift

ADD  modules /opt/eap/modules
ADD  configuration/standalone-openshift.xml /opt/eap/standalone/configuration/standalone-openshift.xml
ADD  cooprp-web/target/cooprp-web /opt/eap/standalone/deployments/cooprp.war
ADD  docker/dodeploy /opt/eap/standalone/deployments/cooprp.war.dodeploy

ENV DEBUG true
ENV TZ Europe/Rome
ENV GESTA_HOST http://cooprp-gesta-service-saw-dev.cloud.coopservice.it
EXPOSE 8787
EXPOSE 8080



