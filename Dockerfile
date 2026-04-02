# ============================================================
# STAGE 1: BUILD
# ============================================================
# Use a Maven image that already has Java 17 installed.
# This image comes with both Maven (build tool) and the JDK
# (Java compiler), so we can compile our Spring Boot project.
# "AS build" gives this stage a name so we can reference it later.
FROM maven:3.9-eclipse-temurin-17 AS build

# Set /app as the working directory inside the container.
# Every command after this will run from /app.
WORKDIR /app

# Copy ONLY the pom.xml first (before copying source code).
# Docker caches each layer — if pom.xml hasn't changed,
# Docker will reuse the cached dependencies layer and skip
# re-downloading everything. This makes rebuilds much faster.
COPY pom.xml .

# Download all Maven dependencies without compiling anything.
# This creates a separate cached layer for dependencies.
# If only your source code changes (not pom.xml), Docker
# skips this step entirely on the next build.
RUN mvn dependency:go-offline -B

# Now copy the actual source code into the container.
COPY src ./src

# Compile the project and package it into a .jar file.
# -DskipTests skips running tests during the Docker build
# because tests (especially integration tests) may need
# a database that doesn't exist yet at build time.
# The output will be: /app/target/pauta-server.jar
RUN mvn clean package -DskipTests

# ============================================================
# STAGE 2: RUN
# ============================================================
# Use a slim JDK image that only has the Java runtime.
# This is much smaller than the Maven image (~300MB vs ~800MB)
# because we don't need Maven or the compiler anymore —
# we just need to RUN the compiled .jar file.
# Using "jammy" (Ubuntu 22.04) instead of "alpine" because
# the alpine variant doesn't have an ARM64 build (Apple Silicon).
FROM eclipse-temurin:17-jre-jammy

# Set /app as the working directory in this new stage.
WORKDIR /app

# Copy ONLY the compiled .jar file from the build stage.
# Everything else (source code, Maven cache, etc.) is
# thrown away. This keeps the final image small and clean.
COPY --from=build /app/target/pauta-server.jar ./pauta-server.jar

# Document that this container listens on port 8081.
# This doesn't actually open the port — it's metadata
# that tells other developers which port the app uses.
# The actual port mapping happens in docker-compose.yml.
EXPOSE 8081

# The command that runs when the container starts.
# "java -jar pauta-server.jar" starts the Spring Boot
# application, which will begin listening for HTTP requests.
CMD ["java", "-jar", "pauta-server.jar"]
