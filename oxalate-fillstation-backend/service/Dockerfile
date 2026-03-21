FROM eclipse-temurin:25-jdk-alpine AS build

WORKDIR /workspace

COPY . .

RUN chmod +x ./gradlew && ./gradlew --no-daemon :service:bootJar

FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY --from=build /workspace/service/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
