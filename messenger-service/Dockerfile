FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/messenger-service.jar messenger-service-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "messenger-service-0.0.1-SNAPSHOT.jar"]
