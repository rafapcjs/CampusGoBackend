# Etapa 1: Compilación
FROM eclipse-temurin:21-jdk AS build

WORKDIR /root

# Copiar archivos de configuración de Maven y el archivo pom.xml
COPY ./pom.xml ./
COPY .mvn/ .mvn
COPY mvnw ./

# Otorgar permisos de ejecución al script mvnw
RUN chmod +x mvnw

# Descargar dependencias necesarias
RUN ./mvnw dependency:go-offline

# Copiar el código fuente y construir la aplicación
COPY ./src ./src
RUN ./mvnw clean package -DskipTests

# Verificar que el JAR se generó correctamente
RUN ls -l /root/target/

# Etapa 2: Imagen final
FROM eclipse-temurin:21-jre
WORKDIR /root

# Copiar el JAR generado desde la etapa de construcción
COPY --from=build /root/target/CampusGo-0.0.1-SNAPSHOT.jar /root/app.jar

# Otorgar permisos de ejecución al JAR
RUN chmod +x /root/app.jar

# Exponer el puerto
EXPOSE 8080

# Definir el comando de inicio
ENTRYPOINT ["java", "-jar", "/root/app.jar"]