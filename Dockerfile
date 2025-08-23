FROM eclipse-temurin:21-jdk

WORKDIR /application

ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod

COPY gradlew .
COPY gradle gradle
COPY config config
COPY build.gradle.kts .
COPY settings.gradle.kts .

COPY src src

RUN ./gradlew build -x test

CMD ["java", "-jar", "build/libs/app-0.0.1-SNAPSHOT.jar"]