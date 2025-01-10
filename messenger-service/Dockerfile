FROM maven:3.9-openjdk-21 AS builder

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/messenger-service.jar messenger-service-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "messenger-service-0.0.1-SNAPSHOT.jar"]
