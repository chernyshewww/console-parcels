# Use an image with Gradle installed to build the JAR
FROM gradle:7.6-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon -x spotlessCheck -x test

# Use a smaller runtime image for the final build
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/console-parcels-billing-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
