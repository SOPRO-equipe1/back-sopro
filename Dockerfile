
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/project_demoday-0.0.1-SNAPSHOT.jar sopro-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "sopro-api.jar"]