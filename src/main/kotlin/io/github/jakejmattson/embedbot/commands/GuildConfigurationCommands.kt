package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.dataclasses.*
import io.github.jakejmattson.embedbot.extensions.requiredPermissionLevel
import io.github.jakejmattson.embedbot.locale.messages
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.arguments.RoleArg
import me.aberrantfox.kjdautils.internal.di.PersistenceService

@CommandSet("GuildConfiguration")
fun guildConfigurationCommands(configuration: Configuration,
                               persistenceService: PersistenceService,
                               embedService: EmbedService) = commands {

    requiredPermissionLevel = Permission.GUILD_OWNER

    command("SetRequiredRole") {
        description = messages.descriptions.SET_REQUIRED_ROLE
        execute(RoleArg) {
            val requiredRole = it.args.component1()
            val guildConfiguration = configuration.getGuildConfig(it.guild!!.id)
                ?: return@execute it.respond(messages.errors.GUILD_NOT_SETUP)

            guildConfiguration.requiredRole = requiredRole.name
            persistenceService.save(configuration)

            it.respond("Required role set to: ${requiredRole.name}")
        }
    }

    command("DeleteAll") {
        description = messages.descriptions.DELETE_ALL
        execute {
            val guild = it.guild!!
            val removed = embedService.removeAllFromGuild(guild)

            it.respond("Successfully deleted $removed embeds.")
        }
    }

    command("Setup") {
        description = messages.descriptions.SETUP
        execute(RoleArg("Required Role")) {
            val requiredRole = it.args.component1()
            val guildConfiguration = configuration.getGuildConfig(it.guild!!.id)

            if (guildConfiguration != null)
                return@execute it.respond(messages.errors.GUILD_ALREADY_SETUP)

            configuration.guildConfigurations.add(GuildConfiguration(it.guild!!.id, requiredRole.name))
            persistenceService.save(configuration)

            it.respond("This guild is now setup for use!")
        }
    }
}