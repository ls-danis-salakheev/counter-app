FROM openjdk:17-jdk-slim
ARG JAR_FILE=*.jar
EXPOSE 8080
COPY /build/libs/${JAR_FILE} counter-1.0.jar
ENTRYPOINT ["java", "-jar", "counter-1.0.jar"]
