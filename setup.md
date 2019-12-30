## Discord Setup

### Server
If you don't already have one, create a Discord server for the bot to run on. 
Follow the [official guide](https://support.discordapp.com/hc/en-us/articles/204849977-How-do-I-create-a-server-) if needed.

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
- Click the link to add it to your server.

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

### Windows

1. Download and install the docker toolbox.
2. Clone this repository: `git clone https://github.com/JakeJMattson/EmbedBot.git` - 
    you can also just download and extract the zip file.
3. Open the command prompt
4. `cd /EmbedBot` - cd into the directory
5. `%CD%/scripts/deploy.bat <YOUR_BOT_TOKEN> <CONFIG_PATH>` 
    - replace <YOUR_BOT_TOKEN> with a valid discord bot token.
    - replace <CONFIG_PATH> with a path to where you want the bot configuration to be.
    
    **Important:** The paths required for a correct deployment on Windows are very specific.
    In order to mount correctly, the folder on your local machine must be within the shared folders of the VM.
    By default, the shared folder list is exclusively `C:\Users`. This includes all subdirectories. 
    It also requires a very specific format - using forward slashes, instead of the traditional Windows format.
    It's recommended to make a folder with a similar path to this: `/c/Users/account/embedbot` to store configurations.
    
6. Example run `%CD%/scripts/deploy.bat aokspdf.okwepofk.34p1o32kpo,pqo.sASDAwd /c/Users/account/embedbot`
   *note: The token is fake :)* 

## Linux

1. Download and install docker.
2. Clone this repository: `git clone https://github.com/JakeJMattson/EmbedBot.git` -
    you can also just download and extract the zip file.
3. Open a terminal or command prompt
4. `cd /EmbedBot` - cd into the directory
5. `./scripts/deploy.sh <YOUR_BOT_TOKEN> <CONFIG_PATH>` 
    - replace <YOUR_BOT_TOKEN> with a valid discord bot token.
    - replace <CONFIG_PATH> with a path to where you want the bot configuration to be.
      It's recommended to just make a folder called `/home/me/config`.
6. Example run `./scripts/deploy.sh aokspdf.okwepofk.34p1o32kpo,pqo.sASDAwd /home/me/config`
   *note: The token is fake :)* 

</details>

## Configuration

```json
{
  "botOwner": "This is your Discord ID",
  "prefix": "This is the prefix that the bot will respond to",
  "githubToken": "This is your GitHub access token used to fetch updates",
  "guildConfigurations": [
    {
      "guildId": "This is the ID of the guild you're configuring",
      "requiredRole": "This is the name of the role required to use the bot"
    }
  ]
}
```

### GitHub Token

In order to use the `update` command, you must provide a GitHub access token so that the bot can fetch the latest release.
This functionality is optional. The bot will function normally if no token is provided, but it will be unable to update itself.
Please follow the [official guide](https://help.github.com/en/github/authenticating-to-github/creating-a-personal-access-token-for-the-command-line#creating-a-token) in order to create an access token.
Note that this operation doesn't require any special permissions, as no information about your account is read or written.
This access token will only be used to download the newest JAR file available.

## Usage Guide

### Sanity Check
Once you have your bot account online, you can verify that it's working by mentioning the bot on Discord `@EmbedBot`. Make sure it has permissions to read and write in the place where you want it to respond. It is recommended that you give it a role.

### Hello World
For this section, let's assume that you kept the default prefix (`=`).
Run these steps to create your first embed:

```
==Create HelloWorld
==SetTitle Hello World
==Send
```

You should now see an embed with the text "Hello World"!
Now you're free to explore and create whatever embeds you want.
Run `==help` to see a full list of available commands.
