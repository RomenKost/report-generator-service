FROM openjdk:11
COPY build/libs/report-generator-service-0.0.1-SNAPSHOT.jar report-generator-service-0.0.1-SNAPSHOT.jar
RUN mkdir "/reports"
VOLUME "/reports"
ENTRYPOINT ["java", "-jar", "report-generator-service-0.0.1-SNAPSHOT.jar"]
