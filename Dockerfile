# ============ STAGE 1: Build ============
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy Maven/Gradle wrapper and dependencies first (for caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (this layer will be cached)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# ============ STAGE 2: Runtime ============
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy only the built jar from the build stage
COPY --from=build /app/target/apigateway-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
