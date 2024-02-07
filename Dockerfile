FROM openjdk:17-jdk

ARG PROFILES
ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-jar", "app.jar"]