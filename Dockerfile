# Use the official Java 17 JDK image from the Docker Hub
FROM openjdk:17-jdk

# Set the working directory inside the container
WORKDIR /opt/app

# Copy the built jar file from the local machine to the container file system
# Make sure to build the jar using 'gradle build' before running docker build
COPY build/libs/*.jar app.jar

# Expose the port the application uses
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
