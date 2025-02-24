FROM openjdk:22
COPY build/libs/76DPSCalculator-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080