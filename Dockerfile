FROM openjdk:11
LABEL maintainer="pat.essiam701@gmail.com"
VOLUME /main-app
ADD target/theam-crm-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 5000
ENTRYPOINT ["java", "-jar","/app.jar"]