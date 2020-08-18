## Discord Setup

Not interested in all the technical stuff? Just [invite](https://discordapp.com/oauth2/authorize?client_id=439163847618592782&scope=bot&permissions=101440) the bot directly.

### Bot Account
Create a bot account in the [developers](https://discordapp.com/developers/applications/me) section of the Discord website.
- Create an application
- Under "General Information" 
	- Enter an app icon and a name.
	- You will need the client ID when adding the bot to a server.
- Under "Bot"
	- Create a bot.
	- Give it a username and app icon. 
	- You will need the token when operating the bot.
		- This is a secret token, don't reveal it!
- Save changes

### Add Bot
- Visit the [permissions](https://discordapi.com/permissions.html) page.
- Under "OAth URL Generator" enter the bot's client ID from the step above.
- Click the link to add it to a server.

## Build Guide

Choose one of the following build options.

<details>
<summary>Source Code</summary>

### Prerequisites
- [Download](https://github.com/JakeJMattson/EmbedBot/archive/master.zip) the code.
- Install [Java](https://openjdk.java.net/) and [Gradle](https://gradle.org/install/).

### Building
Once you have your prerequisites installed, Gradle will be doing the rest.

* Navigate to the root of the project (`./EmbedBot`)
* Run `gradle shadowJar` to build a JAR in `./build/libs/`
* Proceed to the JAR guide below on how to run it.

</details>

<details>
<summary>JAR/Release</summary>

### Prerequisites
- Install [Java](https://openjdk.java.net/).
- Download a JAR from [releases](https://github.com/JakeJMattson/EmbedBot/releases/), unless you built it yourself.

### Environment
- To run the JAR, you will need to be able to access Java from the command line/terminal. Run `java -version` and make sure your operating system can recognize the command.
- Place the JAR somewhere safe and out of the way. It will generate configuration files on its own.

### Running
- Open the command prompt and navigate to the folder that the JAR is in.
- Run the following command: `java -jar EmbedBot.jar <token>`
	- `<token>` should be replaced with your Discord bot token

- The bot should respond that configuration files have been generated. This will be in the `config` folder within the folder you created for this project.
- Open `config.json` with any text editor and fill out the fields. You can read more about this below.
- Run the same command again: `java -jar EmbedBot.jar token`

The JAR will now read in your provided configuration values and start the bot. Your bot account should now be online!

</details>
<details>
<summary>Docker</summary>

New containers for this project are built automatically. Pulling it with Docker will always give you the most recent commit.

* Install [Docker Desktop](https://www.docker.com/get-started) onto your machine.
* Run `docker pull jakejmattson/embedbot`
* Run `docker run -e BOT_TOKEN=<TOKEN> -v <PATH>:/config embedbot:latest`
    * `<TOKEN>` should be replaced with the bot token obtained from the steps above.
    * `<PATH>` should be replaced with a local path where you want to store the configuration files.

Your Docker container should now be running.

</details>

## Configuration

```json
{
  "botOwner": "The discord user with owner permissions.",
  "guildConfigurations": {
    "244230771232079873": {
      "prefix": "The prefix typed before a command.",
      "requiredRoleId": "The ID of the role required to use the bot."
    }
  }
}
```

### Sanity Check
Once you have your bot account online, you can verify that it's working by mentioning the bot on Discord `@EmbedBot`. Make sure it has permissions to read and write in the place where you want it to respond. It is recommended that you give it a role.