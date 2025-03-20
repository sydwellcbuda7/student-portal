# ---- Stage 1: Build ----
FROM maven:3.8.7-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ---- Stage 2: Run ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/student-portal-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]
