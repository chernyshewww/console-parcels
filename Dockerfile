# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
# Replace 'console-parcels-1.0.0.jar' with your actual JAR file name
COPY target/console-parcels-1.0.0.jar app.jar

# Expose the port your application uses (update if different)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
