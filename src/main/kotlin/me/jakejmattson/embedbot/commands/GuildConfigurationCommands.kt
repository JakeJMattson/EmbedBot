package me.jakejmattson.embedbot.commands

import me.jakejmattson.embedbot.dataclasses.Configuration
import me.jakejmattson.embedbot.extensions.requiredPermissionLevel
import me.jakejmattson.embedbot.locale.messages
import me.jakejmattson.embedbot.services.*
import me.jakejmattson.kutils.api.annotations.CommandSet
import me.jakejmattson.kutils.api.arguments.*
import me.jakejmattson.kutils.api.dsl.command.commands

@CommandSet("GuildConfiguration")
fun guildConfigurationCommands(configuration: Configuration, embedService: EmbedService) = commands {
    command("SetPrefix") {
        description = messages.descriptions.SET_PREFIX
        requiredPermissionLevel = Permission.GUILD_OWNER
        execute(AnyArg("Prefix")) {
            val prefix = it.args.first

            configuration[it.guild!!.idLong]?.prefix = prefix
            configuration.save()

            it.respond("Prefix set to: $prefix")
        }
    }

    command("SetRole") {
        description = messages.descriptions.SET_REQUIRED_ROLE
        requiredPermissionLevel = Permission.GUILD_OWNER
        execute(RoleArg) {
            val requiredRole = it.args.first
            val guildConfiguration = configuration[it.guild!!.idLong]
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
}