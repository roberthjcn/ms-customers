# Usa una imagen base de Java 17
FROM eclipse-temurin:17-jdk

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el WAR generado por Gradle al contenedor
COPY build/libs/ms-customers-0.0.1-SNAPSHOT.war app.war

# Exponer el puerto en el que correrá el servicio
EXPOSE 8081

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.war"]