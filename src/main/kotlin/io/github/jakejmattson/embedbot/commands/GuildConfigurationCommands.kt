package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.dataclasses.*
import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.locale.messages
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.arguments.RoleArg
import me.aberrantfox.kjdautils.internal.di.PersistenceService
import net.dv8tion.jda.api.entities.Role

@CommandSet("GuildConfiguration")
fun guildConfigurationCommands(configuration: Configuration,
                               persistenceService: PersistenceService,
                               embedService: EmbedService) = commands {

    requiredPermissionLevel = Permission.GUILD_OWNER

    command("SetRequiredRole") {
        description = messages.descriptions.SET_REQUIRED_ROLE
        expect(RoleArg)
        execute {
            val requiredRole = it.args.component1() as Role
            val guildConfiguration = configuration.getGuildConfig(it.guild!!.id)
                ?: return@execute it.respond("This guild is not set up for use. Please use the `setup` command.")

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
        expect(RoleArg("Required Role"))
        execute {
            val requiredRole = it.args.component1() as Role
            val guildConfiguration = configuration.getGuildConfig(it.guild!!.id)

            if (guildConfiguration != null)
                return@execute it.respond("This guild is already setup for use.")

            configuration.guildConfigurations.add(GuildConfiguration(it.guild!!.id, requiredRole.name))
            persistenceService.save(configuration)

            it.respond("This guild is now setup for use!")
        }
    }
}