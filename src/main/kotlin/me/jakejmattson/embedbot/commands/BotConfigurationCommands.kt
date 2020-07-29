package me.jakejmattson.embedbot.commands

import me.jakejmattson.embedbot.dataclasses.Configuration
import me.jakejmattson.embedbot.extensions.requiredPermissionLevel
import me.jakejmattson.embedbot.locale.messages
import me.jakejmattson.embedbot.services.*
import me.jakejmattson.kutils.api.annotations.CommandSet
import me.jakejmattson.kutils.api.arguments.*
import me.jakejmattson.kutils.api.dsl.command.commands
import kotlin.system.exitProcess

@CommandSet("BotConfiguration")
fun botConfigurationCommands(configuration: Configuration, embedService: EmbedService) = commands {
    command("SetPrefix") {
        description = messages.descriptions.SET_PREFIX
        requiredPermissionLevel = Permission.BOT_OWNER
        execute(AnyArg("Prefix")) {
            val prefix = it.args.first

            configuration.getGuildConfig(it.guild!!.id)?.prefix = prefix
            configuration.save()

            it.respond("Prefix set to: $prefix")
        }
    }

    command("Leave") {
        description = messages.descriptions.LEAVE
        requiredPermissionLevel = Permission.BOT_OWNER
        execute(GuildArg.makeOptional { it.guild!! }) {
            val guild = it.args.first
            val guildConfiguration = configuration.getGuildConfig(guild.id)

            if (guildConfiguration != null) {
                configuration.guildConfigurations.remove(guildConfiguration)
                configuration.save()
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
        description = messages.descriptions.KILL
        requiredPermissionLevel = Permission.BOT_OWNER
        execute {
            it.respond("Goodbye :(")
            exitProcess(0)
        }
    }
}