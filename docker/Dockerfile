FROM maven:3.6.0-jdk-11-slim AS build

COPY pom.xml /pom.xml
COPY src /src/

RUN mvn clean package -f /pom.xml

# Run stage
FROM openjdk:12

ENV BOT_TOKEN=UNSET

RUN mkdir /config/
COPY --from=build /target/EmbedBot-jar-with-dependencies.jar /EmbedBot.jar

CMD /usr/bin/java -jar /EmbedBot.jar $BOT_TOKEN