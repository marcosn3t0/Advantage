FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

RUN mvn dependency:go-offline -B

RUN mvn exec:java -e -D exec.mainClass="com.microsoft.playwright.CLI" -D exec.args="install --with-deps"

RUN mvn compile

CMD ["mvn", "clean", "test"]
