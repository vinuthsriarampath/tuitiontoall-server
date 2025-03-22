# Use Eclipse Temurin with JDK 22
FROM eclipse-temurin:22-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/tuitiontoall-client-2.0.0.jar /app/app.jar

# Expose port
EXPOSE 8080

# Use "exec" to correctly handle container shutdown signals
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
