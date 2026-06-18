# Use Java 21
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy maven files first (for caching)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Copy source code
COPY src src

# Build the app
RUN ./mvnw clean package -DskipTests

# Run the app
EXPOSE 8081
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]