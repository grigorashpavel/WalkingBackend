FROM gradle:jdk17-alpine as builder

WORKDIR /app

COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
RUN gradle dependencies

COPY src src
COPY data/specs/ data/specs/

RUN gradle --no-daemon --stacktrace \
    openApiGenerate \
    bootJar

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8000
ENTRYPOINT ["java", "-jar", "app.jar"]
