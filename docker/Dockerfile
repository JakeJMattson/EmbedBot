FROM gradle:6.7.0-jdk15 AS build
COPY --chown=gradle:gradle . /embedbot
WORKDIR /embedbot
RUN gradle shadowJar --no-daemon

FROM openjdk:8-jre-slim
ENV BOT_TOKEN=UNSET
RUN mkdir /config/
COPY --from=build /embedbot/build/libs/*.jar /EmbedBot.jar

ENTRYPOINT ["java", "-jar", "/EmbedBot.jar", "$BOT_TOKEN"]