package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.dataclasses.Configuration
import io.github.jakejmattson.embedbot.extensions.requiredPermissionLevel
import io.github.jakejmattson.embedbot.locale.messages
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.command.*
import me.aberrantfox.kjdautils.internal.arguments.WordArg
import me.aberrantfox.kjdautils.internal.di.PersistenceService

@CommandSet("BotConfiguration")
fun botConfigurationCommands(configuration: Configuration, prefixService: PrefixService,
                             persistenceService: PersistenceService, embedService: EmbedService) = commands {

    requiredPermissionLevel = Permission.BOT_OWNER

    command("SetPrefix") {
        description = messages.descriptions.SET_PREFIX
        execute(WordArg("Prefix")) {
            val prefix = it.args.component1()

            prefixService.setPrefix(prefix)
            persistenceService.save(configuration)

            it.respond("Prefix set to: $prefix")
        }
    }

    command("ResetBot") {
        description = messages.descriptions.RESET_BOT
        execute(WordArg("Bot Owner ID").makeOptional("")) {
            val idEntry = it.args.component1()
            val ownerId = configuration.botOwner
            val jda = it.discord.jda
            val guildConfigs = configuration.guildConfigurations

            if (idEntry.isEmpty())
                return@execute it.respond(messages.errors.MISSING_RESET_CONFIRMATION)

            if (idEntry != ownerId)
                return@execute it.respond(messages.errors.INVALID_OWNER_ID)

            val removedEmbeds =
                guildConfigs.sumBy { guildConfiguration ->
                    val guild = jda.getGuildById(guildConfiguration.guildId)

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
        description = messages.descriptions.LEAVE
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