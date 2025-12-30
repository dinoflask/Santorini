# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Production stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/santorini-1.0-SNAPSHOT.jar app.jar
EXPOSE 10000
# ✅ FORCE Render PORT=10000
ENTRYPOINT ["java", "-jar", "app.jar"]

