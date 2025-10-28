FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copiar wrapper y archivos de configuración
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# Dar permisos de ejecución al gradlew
RUN chmod +x gradlew

# Descargar dependencias sin compilar el código fuente (para aprovechar cache)
RUN ./gradlew --no-daemon dependencies

# Copiar el código fuente
COPY src ./src

# Construir sin tests
RUN ./gradlew --no-daemon build -x test


FROM eclipse-temurin:17-jre
WORKDIR /app
EXPOSE 8080
ENV JAVA_OPTS=""

# Copiar el jar generado desde el stage anterior
COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
