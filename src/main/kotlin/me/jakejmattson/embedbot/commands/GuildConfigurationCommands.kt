package me.jakejmattson.embedbot.commands

import me.jakejmattson.embedbot.dataclasses.*
import me.jakejmattson.embedbot.extensions.requiredPermissionLevel
import me.jakejmattson.embedbot.locale.messages
import me.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.command.*
import me.aberrantfox.kjdautils.internal.arguments.RoleArg
import me.aberrantfox.kjdautils.internal.di.PersistenceService

@CommandSet("GuildConfiguration")
fun guildConfigurationCommands(configuration: Configuration,
                               persistenceService: PersistenceService,
                               embedService: EmbedService) = commands {

    command("SetRequiredRole") {
        description = messages.descriptions.SET_REQUIRED_ROLE
        requiredPermissionLevel = Permission.GUILD_OWNER
        execute(RoleArg) {
            val requiredRole = it.args.first
            val guildConfiguration = configuration.getGuildConfig(it.guild!!.id)
                ?: return@execute it.respond(messages.errors.GUILD_NOT_SETUP)

            guildConfiguration.requiredRole = requiredRole.name
            persistenceService.save(configuration)

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
        execute(RoleArg("Required Role")) {
            val requiredRole = it.args.first
            val guildConfiguration = configuration.getGuildConfig(it.guild!!.id)

            if (guildConfiguration != null)
                return@execute it.respond(messages.errors.GUILD_ALREADY_SETUP)

            configuration.guildConfigurations.add(GuildConfiguration(it.guild!!.id, requiredRole.name))
            persistenceService.save(configuration)

            it.respond("This guild is now setup for use!")
        }
    }
}