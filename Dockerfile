FROM maven:3.8-openjdk-17-slim

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

CMD ["java", "-jar", "target/student-portal-0.0.1-SNAPSHOT.jar"]
