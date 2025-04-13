FROM openjdk:21-slim
COPY /build/libs/hms-0.0.1-SNAPSHOT.jar hms.jar
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "/hms.jar"]
EXPOSE 8080