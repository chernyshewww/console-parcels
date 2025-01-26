# Use a base image for Java, since these appear to be Java-based applications
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR or class files into the container
COPY ./build/libs/console-parcels-telegram.jar /app/console-parcels-telegram.jar

# Set the entrypoint (adjust if necessary)
ENTRYPOINT ["java", "-jar", "console-parcels.jar"]

# Expose port 8081 for the application to run
EXPOSE 8082
