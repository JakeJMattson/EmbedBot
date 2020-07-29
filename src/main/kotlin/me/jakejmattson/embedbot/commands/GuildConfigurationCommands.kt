package me.jakejmattson.embedbot.commands

import me.jakejmattson.embedbot.conversations.SetupConversation
import me.jakejmattson.embedbot.dataclasses.Configuration
import me.jakejmattson.embedbot.extensions.requiredPermissionLevel
import me.jakejmattson.embedbot.locale.messages
import me.jakejmattson.embedbot.services.*
import me.jakejmattson.kutils.api.annotations.CommandSet
import me.jakejmattson.kutils.api.arguments.*
import me.jakejmattson.kutils.api.dsl.command.commands
import me.jakejmattson.kutils.api.services.*

@CommandSet("GuildConfiguration")
fun guildConfigurationCommands(configuration: Configuration, embedService: EmbedService, conversationService: ConversationService) = commands {
    command("SetPrefix") {
        description = messages.descriptions.SET_PREFIX
        requiredPermissionLevel = Permission.GUILD_OWNER
        execute(AnyArg("Prefix")) {
            val prefix = it.args.first

            configuration.getGuildConfig(it.guild!!.id)?.prefix = prefix
            configuration.save()

            it.respond("Prefix set to: $prefix")
        }
    }

    command("SetRole") {
        description = messages.descriptions.SET_REQUIRED_ROLE
        requiredPermissionLevel = Permission.GUILD_OWNER
        execute(RoleArg) {
            val requiredRole = it.args.first
            val guildConfiguration = configuration.getGuildConfig(it.guild!!.id)
                ?: return@execute it.respond(messages.errors.GUILD_NOT_SETUP)

            guildConfiguration.requiredRoleId = requiredRole.idLong
            configuration.save()

            it.respond("Required role set to: ${requiredRole.name}")
        }
    }

    command("DeleteAll") {
        description = messages.descriptions.DELETE_ALL
        requiredPermissionLevel = Permission.GUILD_OWNER
        execute {
            val guild = it.guild!!
            val removed = embedService.removeAllFromGuild(guild)

            it.respond("Successfully deleted $removed embeds.")
        }
    }

    command("Setup") {
        description = messages.descriptions.SETUP
        requiredPermissionLevel = Permission.GUILD_OWNER
        execute {
            val response = when (conversationService.startPublicConversation<SetupConversation>(it.author, it.channel, it.guild!!)) {
                ConversationResult.COMPLETE -> "Guild setup complete!"
                ConversationResult.EXITED -> "Guild setup cancelled!"
                ConversationResult.HAS_CONVO -> "Already running setup conversation."
                else -> "A machine has outsmarted a human and an impossible thing has happened."
            }

            it.respond(response)
        }
    }
}