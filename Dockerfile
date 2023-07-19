FROM eclipse-temurin:17-jdk-alpine

# Add app user
ARG APPLICATION_USER=appuser
RUN adduser --no-create-home -u 1000 -D $APPLICATION_USER

# Configure working directory
RUN mkdir /app && \
    chown -R $APPLICATION_USER /app

USER 1000

COPY --chown=1000:1000 ./target/MedicalRecords-0.0.1-SNAPSHOT.jar /app/app.jar
WORKDIR /app

EXPOSE 9090
ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]

# Installation de Mongo

# Configuration de MongoDB Cloud
COPY .env .env

## Définir la commande pour exécuter l'API
#CMD ["java", "-jar", "api.jar"]


# Commande pour substituer les variables d'environnement et lancer votre application Spring Boot
CMD envsubst < .env > .env && java -jar /app.jar