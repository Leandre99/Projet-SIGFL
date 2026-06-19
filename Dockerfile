# =========================================================================
# Multi-stage build for the SIGFL Spring Boot backend.
# Stage 1 builds a layered jar; stage 2 ships a slim JRE runtime image.
# =========================================================================

# ---- Build stage ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# Cache dependencies first: copy only what's needed to resolve them.
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw -B -q dependency:go-offline

# Now copy sources and build (skip tests; they need Docker/Testcontainers).
COPY src/ src/
RUN ./mvnw -B -q clean package -DskipTests \
    && cp target/*.jar target/app.jar \
    && java -Djarmode=tools -jar target/app.jar extract --layers --launcher --destination target/extracted

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Run as a non-root user.
RUN groupadd --system spring && useradd --system --gid spring spring
USER spring:spring

# Copy the exploded layers in cache-friendly order (least to most volatile).
ARG EXTRACTED=/workspace/target/extracted
COPY --from=build ${EXTRACTED}/dependencies/ ./
COPY --from=build ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=build ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=build ${EXTRACTED}/application/ ./

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
