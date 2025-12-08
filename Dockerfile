FROM eclipse-temurin:21-jdk
LABEL authors="david"

# Argumento para pasar el nombre del archivo JAR (esto lo hace genérico)
ARG JAR_FILE=target/jellyfinplus-1.0-SNAPSHOT.jar

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR generado en el contenedor
COPY ${JAR_FILE} app.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","/app/app.jar"]
