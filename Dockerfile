# ------------ 1. BUILD STAGE ------------
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copy Maven wrapper and give permissions
#COPY mvnw .
#COPY .mvn .mvn
RUN #chmod +x ./mvnw

# Copy the entire project
COPY . .

# Build without tests
RUN ./mvnw clean package # RUN ./mvnw -B -DskipTests clean package


# ------------ 2. RUNTIME STAGE ------------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy built jar
COPY --from=build /app/target/*.jar app.jar

# Render sets PORT automatically
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
