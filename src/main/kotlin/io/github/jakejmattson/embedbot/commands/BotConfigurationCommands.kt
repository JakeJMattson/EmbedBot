package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.dataclasses.*
import io.github.jakejmattson.embedbot.discordToken
import io.github.jakejmattson.embedbot.extensions.requiredPermissionLevel
import io.github.jakejmattson.embedbot.locale.messages
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.command.*
import me.aberrantfox.kjdautils.internal.arguments.WordArg
import me.aberrantfox.kjdautils.internal.di.PersistenceService
import kotlin.system.exitProcess

@CommandSet("BotConfiguration")
fun botConfigurationCommands(configuration: Configuration, prefixService: PrefixService, gitHubService: GitHubService,
                             persistenceService: PersistenceService, embedService: EmbedService) = commands {

    command("SetPrefix") {
        description = messages.descriptions.SET_PREFIX
        requiredPermissionLevel = Permission.BOT_OWNER
        execute(WordArg("Prefix")) {
            val prefix = it.args.first

            prefixService.setPrefix(prefix)
            persistenceService.save(configuration)

            it.respond("Prefix set to: $prefix")
        }
    }

    command("ResetBot") {
        description = messages.descriptions.RESET_BOT
        requiredPermissionLevel = Permission.BOT_OWNER
        execute(WordArg("Bot Owner ID").makeOptional("")) {
            val idEntry = it.args.first
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
        requiredPermissionLevel = Permission.BOT_OWNER
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

    command("Kill") {
        description = "Kill the bot. It will remember this decision."
        execute {
            it.respond("Goodbye :(")
            exitProcess(0)
        }
    }

    command("Restart") {
        description = "Restart the bot via the JAR file."
        execute {
            val currentJar = getFileSystemLocation()

            if (currentJar.extension != ".jar")
                return@execute it.respond("Could not restart. The bot needs to be running from a JAR.")

            it.respond("Restarting...")
            startJar(currentJar.path)
        }
    }

    command("Update") {
        description = "Update the bot to the latest version."
        execute {
            it.respond("Update in progress...")

            val updateResponse = gitHubService.update()

            if (!updateResponse.wasSuccessful)
                return@execute it.respond(updateResponse.message)

            it.respond("Download complete. Proceeding with update.")
            startJar(updateResponse.message)
        }
    }
}

private fun startJar(path: String) {
    val command = "java -jar $path $discordToken"
    Runtime.getRuntime().exec(command)
    exitProcess(0)
}