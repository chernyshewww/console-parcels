# Stage 1: Build the application
FROM openjdk:21-jdk-slim AS build
WORKDIR /app
COPY . /app
RUN chmod +x ./gradlew && ./gradlew clean build --no-daemon -x spotlessCheck  -x test -x compileTestJava

# Stage 2: Extract the JAR layers
FROM openjdk:21-jdk-slim AS extract
WORKDIR /app
COPY --from=build /app/build/libs/console-parcels-1.0.0.jar /app/console-parcels.jar
RUN java -Djarmode=layertools -jar console-parcels.jar extract

# Stage 3: Create the final runtime image
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copy the extracted layers from the extract stage
COPY --from=extract /app/dependencies/ ./
COPY --from=extract /app/spring-boot-loader/ ./
COPY --from=extract /app/snapshot-dependencies/ ./
COPY --from=extract /app/application/ ./

# Set the entry point to launch the Spring Boot application
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

