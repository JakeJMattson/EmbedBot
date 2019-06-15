package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.dataclasses.Configuration
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.WordArg
import me.aberrantfox.kjdautils.internal.di.PersistenceService

@CommandSet("BotConfiguration")
fun botConfigurationCommands(configuration: Configuration, prefixService: PrefixService,
                             persistenceService: PersistenceService, embedService: EmbedService) = commands {
    command("SetPrefix") {
        requiresGuild = true
        description = "Set the prefix required for the bot to register a command."
        expect(WordArg("Prefix"))
        execute {
            val prefix = it.args.component1() as String

            prefixService.setPrefix(prefix)
            persistenceService.save(configuration)

            it.respond("Prefix set to: $prefix")
        }
    }

    command("NukeAllSeriouslyEverythingAllTheData") {
        requiresGuild = true
        description = "Delete all embeds in all guilds. Delete all guild configurations."
        execute {
            val jda = it.jda
            val guildConfigs = configuration.guildConfigurations
            var removedEmbeds = 0

            configuration.guildConfigurations.forEach {
                val guild = jda.getGuildById(it.guildId)

                if (guild != null)
                    removedEmbeds += embedService.removeAllFromGuild(guild)
            }

            val removedGuilds = guildConfigs.size
            guildConfigs.clear()
            persistenceService.save(configuration)

            it.respond("Deleted $removedGuilds guild configurations and $removedEmbeds embeds.")
        }
    }
}