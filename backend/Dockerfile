# Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies first (cacheable layer)
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn package -DskipTests -q

# Runtime stage — imagem mínima, sem Maven, sem JDK, só JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Usuário não-root. Em prod, NUNCA rode como root.
RUN addgroup -S agronorte && adduser -S agronorte -G agronorte
USER agronorte

COPY --from=build /app/target/*.jar app.jar

# JVM tuning para container (não usa 1/4 da RAM da VM, usa 75% do cgroup)
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseZGC -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
