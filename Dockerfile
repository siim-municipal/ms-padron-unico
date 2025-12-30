FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app


ARG JAR_FILE

COPY ${JAR_FILE} app.war

EXPOSE 8080

# Variables de entorno por defecto
ENV JAVA_OPTS=""

# Ejecutamos
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.war"]