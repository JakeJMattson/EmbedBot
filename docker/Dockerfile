FROM maven:3.6.3-openjdk-15 AS build

COPY pom.xml /pom.xml
COPY src /src/

RUN mvn clean package -f /pom.xml

ENV BOT_TOKEN=UNSET

RUN mkdir /config/
COPY --from=build /target/EmbedBot-jar-with-dependencies.jar /EmbedBot.jar

CMD /usr/bin/java -jar /EmbedBot.jar $BOT_TOKEN