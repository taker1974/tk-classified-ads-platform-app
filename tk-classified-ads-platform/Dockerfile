FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/tk-classified-ads-platform-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
