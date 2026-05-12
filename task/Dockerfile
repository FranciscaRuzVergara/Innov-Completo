# Usamos una imagen ligera de Java 17
FROM eclipse-temurin:17-jdk-jammy

# Apuntamos a la carpeta target y copiamos el archivo .jar que generó Maven
COPY target/*.jar app.jar

# Le decimos a Docker cómo ejecutar tu microservicio
ENTRYPOINT ["java", "-jar", "/app.jar"]