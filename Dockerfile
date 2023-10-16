FROM eclipse-temurin:17.0.8.1_1-jdk AS builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM eclipse-temurin:17.0.8.1_1-jre
COPY --from=builder build/libs/*.jar app.jar
ENV	PROFILE test

ENTRYPOINT ["java","-Dspring.profiles.active=${PROFILE}", "-jar", "/app.jar"]

