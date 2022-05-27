FROM navikt/java:17-appdynamics
COPY target/dsop-api.jar app.jar
ENV APPD_ENABLED=true
EXPOSE 8080
