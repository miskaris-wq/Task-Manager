FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

COPY src src

RUN ./gradlew build -x test

CMD ["java", "-jar", "build/libs/app-*.jar"]