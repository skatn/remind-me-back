FROM openjdk:17

COPY ./build/libs/remind-me-back-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

CMD ["--spring.profiles.active=prod"]