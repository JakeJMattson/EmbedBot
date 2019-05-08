# Setup Guide

## Installation Guide

### Server Setup
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
	- Uncheck the option to let it be a public bot so that only you can add it to servers.
- Save changes

### Add Bot
- Visit the [permissions](https://discordapi.com/permissions.html) page.
- Under "OAth URL Generator" enter the bot's client ID that you got earlier.
- Click the link to add it to your server.
- It is recommended to place it at the top of your server so members can see it.

## Build Guide

### Prerequistes
- [Download](https://github.com/JakeJMattson/RoleBot/archive/master.zip) this repository to your machine.
- Install [Java](https://www.oracle.com/technetwork/java/javase/downloads/index.html) JDK 8 or greater.
- Install [IntelliJ](https://www.jetbrains.com/idea/download/#section=windows) or another Maven compatible IDE.

### Building
Once you have your prerequistes installed, Maven will be used to handle all of the other dependencies and build the project.
If you downloaded IntelliJ, building with Maven is supported out of the box. Please read the [Maven import guide](https://www.jetbrains.com/help/idea/2018.3/maven-support.html#maven_import_project_start) if you're unfamiliar with this process.

## Running
If all went well, your bot instance should now be running! You can now run any of the [available commands](Commands.md).
