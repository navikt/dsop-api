FROM navikt/java:17-appdynamics
COPY target/dsop-api-*-jar-with-dependencies.jar /app/app.jar
ENV APPD_ENABLED=true
EXPOSE 8080
