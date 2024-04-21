FROM ubuntu:latest as build

# Install Java
RUN apt update && \
    apt install -y wget software-properties-common && \
    wget https://corretto.aws/downloads/latest/amazon-corretto-22-x64-linux-jdk.deb -O corretto-22.deb && \
    apt install -y ./corretto-22.deb && \
    apt install -y iputils-ping net-tools

# Set up working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw mvnw.cmd pom.xml .mvn . /app/

# Install Maven dependencies
RUN ./mvnw wrapper:wrapper
RUN ./mvnw dependency:go-offline

# Copy source code and build the application
COPY src /app/src
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the runtime image
FROM ubuntu:latest

# Install Java Runtime Environment
RUN apt update && \
    apt install -y wget software-properties-common && \
    wget https://corretto.aws/downloads/latest/amazon-corretto-22-x64-linux-jdk.deb -O corretto-22.deb && \
    apt install -y ./corretto-22.deb && \
    apt install -y iputils-ping net-tools

WORKDIR /app

# Copy the built JAR file
COPY --from=build /app/target/athena_server.jar /app/athena_server.jar

# Expose port 8080
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "athena_server.jar"]