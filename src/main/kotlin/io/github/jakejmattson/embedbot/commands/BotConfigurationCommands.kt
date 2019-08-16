package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.dataclasses.Configuration
import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.arguments.WordArg
import me.aberrantfox.kjdautils.internal.di.PersistenceService

@CommandSet("BotConfiguration")
fun botConfigurationCommands(configuration: Configuration, prefixService: PrefixService,
                             persistenceService: PersistenceService, embedService: EmbedService) = commands {

    requiredPermissionLevel = Permission.BOT_OWNER

    command("SetPrefix") {
        description = "Set the prefix required for the bot to register a command."
        expect(WordArg("Prefix"))
        execute {
            val prefix = it.args.component1() as String

            prefixService.setPrefix(prefix)
            persistenceService.save(configuration)

            it.respondSuccess("Prefix set to: $prefix")
        }
    }

    command("ResetBot") {
        description = "Delete all embeds in all guilds. Delete all guild configurations."
        expect(arg(WordArg("Bot Owner ID"), optional = true, default = ""))
        execute {
            val idEntry = it.args.component1() as String
            val ownerId = configuration.botOwner
            val jda = it.discord.jda
            val guildConfigs = configuration.guildConfigurations

            if (idEntry.isEmpty())
                return@execute it.respond("Please re-run this command and pass in the bot owner ID to confirm. " +
                    "This deletes all embeds and all clusters in all guilds, and clears all guild configurations.")

            if (idEntry != ownerId)
                return@execute it.respond("Invalid bot owner ID.")

            val removedEmbeds =
                guildConfigs.sumBy {
                    val guild = jda.getGuildById(it.guildId)

                    if (guild != null)
                        embedService.removeAllFromGuild(guild)
                    else
                        0
                }

            guildConfigs.clear()
            persistenceService.save(configuration)

            it.respond("Deleted ${guildConfigs.size} guild configurations and $removedEmbeds embeds.")
        }
    }

    command("Leave") {
        description = "Leave this guild and delete all associated information."
        execute {
            val guild = it.guild!!
            val guildConfiguration = configuration.getGuildConfig(guild.id)

            if (guildConfiguration != null) {
                configuration.guildConfigurations.remove(guildConfiguration)
                persistenceService.save(configuration)
            }

            val removedEmbeds = embedService.removeAllFromGuild(guild)
            val removedClusters = guild.getGuildEmbeds().clusterList.size
            it.respond("Deleted all ($removedEmbeds) embeds." +
                    "\nDeleted all ($removedClusters) clusters." +
                    "\nDeleted guild configuration for `${guild.name}`." +
                    "\nLeaving guild. Goodbye.")

            guild.leave().queue()
        }
    }
}