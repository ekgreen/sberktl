FROM openjdk:8-jdk-alpine

ARG SPRING_PROFILE=prod
ENV SPRING_PROFILE="${SPRING_PROFILE}"

EXPOSE 8080

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

CMD ["--spring.profiles.active=${SPRING_PROFILE}"]