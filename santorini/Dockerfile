# Multi-stage: build JAR, then run
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Production image

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/santorini-1.0-SNAPSHOT.jar app.jar
EXPOSE 10000

# Pass Render's PORT env var to Java
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]
