# syntax=docker/dockerfile:1
# ==========================================
# ETAPA 1: Compilar la aplicación
# ==========================================
FROM maven:3.9-eclipse-temurin-25-noble AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY docs/openapi ./docs/openapi

RUN --mount=type=secret,id=maven_settings,target=/root/.m2/settings.xml,required=true \
    --mount=type=secret,id=github_packages_token,env=GITHUB_PACKAGES_TOKEN,required=true \
    mvn --settings /root/.m2/settings.xml clean package -DskipTests


# ==========================================
# ETAPA 2: Ejecutar la aplicación
# ==========================================
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]