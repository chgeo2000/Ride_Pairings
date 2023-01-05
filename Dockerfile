FROM adoptopenjdk/openjdk11:alpine-jre

COPY ./build/libs/RidePairings-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]