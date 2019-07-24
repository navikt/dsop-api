FROM navikt/java:11
COPY target/dsop-api-*-jar-with-dependencies.jar /app/app.jar
