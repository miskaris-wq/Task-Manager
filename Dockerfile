# Стадия сборки
FROM gradle:8.7-jdk21 AS builder

WORKDIR /workspace

# Копируем build.gradle.kts и settings.gradle.kts из app
COPY app/build.gradle.kts app/settings.gradle.kts ./

# Копируем папку gradle из app
COPY app/gradle ./gradle

# Копируем исходники
COPY app/src ./src

# Запускаем сборку с shadowJar
RUN gradle --no-daemon shadowJar

# Финальная стадия
FROM eclipse-temurin:21-jre

WORKDIR /app

# Копируем готовый jar из стадии builder
COPY --from=builder /workspace/build/libs/app.jar .

ENV PORT=8080
EXPOSE $PORT

CMD ["java", "-jar", "app.jar"]
