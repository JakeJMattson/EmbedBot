package me.jakejmattson.embedbot.locale

val messages = Messages()

class Messages(
    val descriptions: Descriptions = Descriptions(),
    val errors: Errors = Errors(),
    val links: Links = Links()
)

class Descriptions(
    //General
    val BOT: String = "A bot for creating and managing embeds.",

    //Bot Configuration
    val LEAVE: String = "Leave this guild and delete all associated information.",
    val RESET_BOT: String = "Delete all embeds in all guilds. Delete all guild configurations.",
    val SET_PREFIX: String = "Set the prefix required for the bot to register a command.",
    val KILL: String = "Kill the bot. It will remember this decision.",
    val RESTART: String = "Restart the bot via the JAR file.",
    val UPDATE: String = "Update the bot to the latest version.",

    //Guild Configuration
    val DELETE_ALL: String = "Delete all embeds and clusters in this guild.",
    val SET_REQUIRED_ROLE: String = "Set the role required to use this bot.",
    val SETUP: String = "Set up this bot for use.",

    //Core
    val CREATE: String = "Create a new embed with this name.",
    val DELETE: String = "Delete the embed with this name.",
    val DUPLICATE: String = "Create a new embed from an existing embed.",
    val EXPORT: String = "Export the currently loaded embed to JSON.",
    val IMPORT: String = "Import a JSON String as an embed.",
    val LOAD: String = "Load the embed with this name into memory.",
    val SEND: String = "Send the currently loaded embed.",

    //Copy
    val COPY_TARGET: String = "Copy an embed by its message ID.",
    val UPDATE_ORIGINAL: String = "Update the original embed this content was copied from.",
    val UPDATE_TARGET: String = "Replace the target message embed with the loaded embed.",

    //Field
    val ADD_BLANK_FIELD: String = "Add a blank field to the loaded embed.",
    val ADD_FIELD: String = "Add a field in the following format: title|body|inline",
    val EDIT_FIELD: String = "Edit a field at a given index with the given data.",
    val EDIT_FIELD_INLINE: String = "Get a field by its index and edit its inline value.",
    val EDIT_FIELD_TEXT: String = "Get a field by its index and edit its text value.",
    val EDIT_FIELD_TITLE: String = "Get a field by its index and edit its title value.",
    val INSERT_FIELD: String = "Insert a field at an index to the loaded embed.",
    val REMOVE_FIELD: String = "Remove a field from the loaded embed by its index.",

    //Cluster
    val ADD_TO_CLUSTER: String = "Add an embed into a cluster.",
    val CLONE_CLUSTER: String = "Clone a group of embeds into a cluster.",
    val CREATE_CLUSTER: String = "Create a cluster for storing and deploying groups of embeds.",
    val DELETE_CLUSTER: String = "Delete a cluster and all of its embeds.",
    val DEPLOY: String = "Deploy a cluster into a target channel.",
    val INSERT_INTO_CLUSTER: String = "Insert an embed into a cluster at an index.",
    val REMOVE_FROM_CLUSTER: String = "Remove embeds from their current cluster.",
    val RENAME_CLUSTER: String = "Change the name of an existing cluster.",
    val UPDATE_CLUSTER: String = "Update the original embeds this cluster was copied from.",

    //Edit
    val CLEAR: String = "Clear a target field from the loaded embed.",
    val RENAME: String = "Change the name of an existing embed.",
    val SET_AUTHOR: String = "Set the author for the currently loaded embed.",
    val SET_COLOR: String = "Set the color for the currently loaded embed.",
    val SET_DESCRIPTION: String = "Set the description for the currently loaded embed.",
    val SET_FOOTER: String = "Set the footer for the currently loaded embed.",
    val SET_IMAGE: String = "Set the image for the currently loaded embed.",
    val SET_THUMBNAIL: String = "Set the thumbnail for the currently loaded embed.",
    val SET_TIMESTAMP: String = "Set the timestamp for the currently loaded embed.",
    val SET_TITLE: String = "Set the title for the currently loaded embed.",

    //Information
    val INFO: String = "Get extended info for the target embed.",
    val LIMITS: String = "Display the discord embed limits.",
    val LIST_EMBEDS: String = "List all embeds created in this guild.",

    //Utility
    val STATUS: String = "Display network status and total uptime."
)

class Errors(
    //General
    val NO_ARGS: String = "No program arguments provided. Expected bot token.",
    val MISSING_GUILD: String = "Must be invoked inside a guild.",
    val EMBED_ALREADY_EXISTS: String = "An embed with this name already exists.",
    val CLUSTER_ALREADY_EXISTS: String = "A cluster with this name already exists.",

    //Commands
    val MISSING_RESET_CONFIRMATION: String = "Please re-run this command and pass in the bot owner ID to confirm.",
    val INVALID_CLUSTER_SIZE: String = "Cluster size should be 1 or greater.",
    val INVALID_MESSAGE_ID: String = "Could not find a message with that ID in the target channel.",
    val INVALID_OWNER_ID: String = "Invalid bot owner ID.",
    val NO_SUCH_CLUSTER: String = "No such cluster with this name.",
    val NOT_COPIED: String = "This embed was not copied from another message.",
    val MISSING_OPTIONAL_EMBED: String = "Please load an embed or specify one explicitly.",
    val GUILD_NOT_SETUP: String = "This guild is not set up for use. Please use the `setup` command.",
    val GUILD_ALREADY_SETUP: String = "This guild is already setup for use.",

    //Validation Service
    val EMPTY_PREFIX: String = "The configured prefix is empty.",
    val EMPTY_BOT_OWNER: String = "The botOwner field is empty.",
    val INVALID_USER: String = "Cannot resolve the botOwner ID to a user.",

    //Preconditions
    val MISSING_CLEARANCE: String = "Missing clearance to use this command.",
    val MISSING_EMBED: String = "No embed loaded!",
    val NO_FIELDS: String = "This embed has no fields.",

    //Embed Update
    val NO_CHANNEL: String = "Target channel does not exist.",
    val NO_MESSAGE: String = "Target message does not exist.",
    val NOT_AUTHOR: String = "Target message is not from this bot.",
    val EMPTY_EMBED: String = "Cannot build an empty embed.",
    val NO_EMBED_IN_MESSAGE: String = "Target message has no embed.",
    val UP_TO_DATE: String = "This message is up to date."
)

class Links(
    //Discord
    val DISCORD_EMBED_DOCS: String = "https://discordapp.com/developers/docs/resources/channel#embed-limits-limits",
    val DISCORD_ACCOUNT: String = "https://discordapp.com/users/254786431656919051/"
)