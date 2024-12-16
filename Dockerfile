FROM ubuntu:20.04

# Set non-interactive mode for apt to avoid prompts
ENV DEBIAN_FRONTEND=noninteractive

# Update package list and install essential dependencies
RUN apt-get update && apt-get install -y \
    software-properties-common \
    curl && \
    rm -rf /var/lib/apt/lists/*

# Install Java (OpenJDK 17 or any other version you prefer)
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk && \
    rm -rf /var/lib/apt/lists/*

# Install Maven
RUN apt-get update && apt-get install -y \
    maven && \
    rm -rf /var/lib/apt/lists/*

# Add deadsnakes PPA for Python 3.9
RUN add-apt-repository ppa:deadsnakes/ppa && \
    apt-get update && apt-get install -y \
    python3.9 python3.9-venv python3.9-dev && \
    rm -rf /var/lib/apt/lists/*


# Find and configure the correct Java binary path
RUN JAVA_PATH=$(update-alternatives --list java | head -n 1) && \
    if [ -n "$JAVA_PATH" ]; then \
        update-alternatives --set java "$JAVA_PATH"; \
    else \
        echo "Java binary not found" && exit 1; \
    fi

# Configure Python alternatives
RUN update-alternatives --install /usr/bin/python3 python3 /usr/bin/python3.9 1


# Verify installations
RUN java -version && \
    mvn -version && \
    python3 --version

# Set working directory
WORKDIR /app

# Copy application files
COPY ./app /app

# build the Java application
RUN cd /app/javaParser && mvn package

ENTRYPOINT ["python3", "main.py"]
