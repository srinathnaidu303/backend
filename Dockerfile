# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jdk

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/hospital-management-system-0.0.1-SNAPSHOT.jar app.jar

# Set the server port as an environment variable (Render sets this)
ENV PORT=8080
EXPOSE 8080

# Run the application with memory limits for the free tier
ENTRYPOINT ["java", "-Xmx384m", "-jar", "app.jar"]
