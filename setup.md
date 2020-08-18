## Discord Setup

### Bot Account
Create a bot account in the [developers](https://discordapp.com/developers/applications/me) section of the Discord website.
- Create an application
- Under "General Information" 
	- Enter an app icon and a name.
	- You will need the client ID for later in this guide; copy it somewhere.
- Under "Bot"
	- Create a bot.
	- Give it a username, app icon, and record the token for future use.
		- Note: This is a secret token, don't reveal it!
- Save changes

### Add Bot
- Visit the [permissions](https://discordapi.com/permissions.html) page.
- Under "OAth URL Generator" enter the bot's client ID that you got earlier.
- Click the link to add it to a server.

## Build Guide

Choose one of the following build options.

<details>
<summary>From Source</summary>

### Prerequisites
- [Download](https://github.com/JakeJMattson/EmbedBot/archive/master.zip) this repository to your machine.
- Install [Java](https://www.oracle.com/technetwork/java/javase/downloads/index.html) JDK 8 or greater.
- Install [IntelliJ](https://www.jetbrains.com/idea/download/#section=windows) or another Maven compatible IDE.

### Building
Once you have your prerequisites installed, Maven will be used to handle all of the other dependencies and build the project.
If you downloaded IntelliJ, building with Maven is supported out of the box. Please read the [Maven import guide](https://www.jetbrains.com/help/idea/2018.3/maven-support.html#maven_import_project_start) if you're unfamiliar with this process.
This will generate a configuration file to fill out. You can read more about those fields below. Once that's done, you can run it again.

## Running
If all went well, your bot instance should now be running!

</details>

<details>
<summary>With a JAR</summary>

### Prerequisites
- Install [Java](https://www.oracle.com/technetwork/java/javase/downloads/index.html) JDK 8 or greater.
- Download one of the [releases](https://github.com/JakeJMattson/EmbedBot/releases/) (preferably the most recent one).

### Environment
- To run the JAR, you will need to be able to access Java from the command line/terminal. Run `java -version` and make sure your operating system can recognize the command.
- Place the JAR somewhere in its own folder, as it will generate configuration files. It is recommended that you put it somewhere out of the way instead of in the desktop/downloads folder.
- Make sure you have your bot token ready. This will be passed into the program in order to control your bot.

### Running
- Open the command prompt in the folder that the JAR is in.
- Run the following command: `java -jar EmbedBot.jar <token>`
	- `<token>` should be replaced with your Discord bot token

- The bot should respond that configuration files have been generated. This will be in the `config` folder within the folder you created for this project.
- Open `config.json` with any text editor and fill out the fields. You can read more about this below.
- Run the same command again: `java -jar EmbedBot.jar token`

The JAR will now read in your provided configuration values and start the bot. Your bot account should now be online!

</details>
<details>
<summary>With Docker</summary>

Each time the code is pushed, a new Docker container is built, meaning you can just pull down the most recent one.

* Install [Docker Desktop](https://www.docker.com/get-started) onto your machine.
* Run `docker pull jakejmattson/embedbot`

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