package me.jakejmattson.embedbot.commands

import me.jakejmattson.discordkt.api.annotations.CommandSet
import me.jakejmattson.discordkt.api.arguments.*
import me.jakejmattson.discordkt.api.dsl.command.commands
import me.jakejmattson.embedbot.dataclasses.Configuration
import me.jakejmattson.embedbot.extensions.requiredPermissionLevel
import me.jakejmattson.embedbot.services.*

@CommandSet("GuildConfiguration")
fun guildConfigurationCommands(configuration: Configuration, embedService: EmbedService) = commands {
    command("SetPrefix") {
        description = "Set the prefix required for the bot to register a command."
        requiredPermissionLevel = Permission.GUILD_OWNER
        execute(AnyArg("Prefix")) {
            val prefix = it.args.first

            configuration[it.guild!!.idLong]?.prefix = prefix
            configuration.save()

            it.respond("Prefix set to: $prefix")
        }
    }

    command("SetRole") {
        description = "Set the role required to use this bot."
        requiredPermissionLevel = Permission.GUILD_OWNER
        execute(RoleArg) {
            val requiredRole = it.args.first

            configuration[it.guild!!.idLong]?.requiredRoleId = requiredRole.idLong
            configuration.save()

            it.respond("Required role set to: ${requiredRole.name}")
        }
    }

    command("DeleteAll") {
        description = "Delete all embeds and groups in this guild."
        requiredPermissionLevel = Permission.GUILD_OWNER
        execute {
            val guild = it.guild!!
            val removed = embedService.removeAllFromGuild(guild)

            it.respond("Successfully deleted $removed embeds.")
        }
    }
}