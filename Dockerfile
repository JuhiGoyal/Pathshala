# Step 1: Build Stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only the pom.xml first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy the source
COPY src ./src

# Build the project (will generate jar in /app/target)
RUN mvn clean package -DskipTests

# Step 2: Run Stage
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the generated jar from previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java","-jar","app.jar"]
