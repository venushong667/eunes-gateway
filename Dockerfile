FROM gradle:8.0.1-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon 

#
# Package stage
#
FROM openjdk:17-jdk-slim

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/eunes-gateway.jar

WORKDIR /app

ENTRYPOINT ["java", "-jar", "eunes-gateway.jar"]