# Etapa 1: build con Maven y Java
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copiamos los archivos necesarios para resolver dependencias
COPY pom.xml .
COPY src ./src

# Compilamos el proyecto y generamos el .jar
RUN mvn clean package -DskipTests

# Etapa 2: imagen más ligera solo con el .jar
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiar el JAR desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
