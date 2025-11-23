# ------------ 1. BUILD STAGE ------------
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Copier tout le projet
COPY . .

# Construire le jar sans exécuter les tests
RUN ./gradlew clean build -x test


# ------------ 2. RUNTIME STAGE ------------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copier le jar compilé
COPY --from=build /app/build/libs/*.jar app.jar

# Render impose d'utiliser la variable d'env $PORT
ENV PORT=8080
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
