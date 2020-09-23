package me.jakejmattson.embedbot.commands

import me.jakejmattson.discordkt.api.annotations.CommandSet
import me.jakejmattson.discordkt.api.arguments.*
import me.jakejmattson.discordkt.api.dsl.command.commands
import me.jakejmattson.discordkt.api.extensions.jda.sendPrivateMessage
import me.jakejmattson.embedbot.arguments.EmbedArg
import me.jakejmattson.embedbot.dataclasses.Configuration
import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.services.*
import kotlin.system.exitProcess

@CommandSet("Owner")
fun botConfigurationCommands(configuration: Configuration, embedService: EmbedService) = commands {
    command("Leave") {
        description = "Leave this guild and delete all associated information."
        requiredPermissionLevel = Permission.BOT_OWNER
        execute(GuildArg.makeOptional { it.guild!! }) {
            val guild = it.args.first

            configuration.guildConfigurations.remove(guild.idLong)
            configuration.save()

            val removedEmbeds = embedService.removeAllFromGuild(guild)
            val removedGroups = guild.getGuildEmbeds().groupList.size

            it.respond("Deleted all ($removedEmbeds) embeds." +
                "\nDeleted all ($removedGroups) groups." +
                "\nDeleted guild configuration for `${guild.name}`." +
                "\nLeaving guild. Goodbye.")

            guild.leave().queue()
            it.reactSuccess()
        }
    }

    command("Kill") {
        description = "Kill the bot. It will remember this decision."
        requiredPermissionLevel = Permission.BOT_OWNER
        execute {
            it.respond("Goodbye :(")
            exitProcess(0)
        }
    }

    command("Broadcast") {
        description = "Send a direct message to all guild owners."
        requiredPermissionLevel = Permission.BOT_OWNER
        execute(EveryArg("Message")) {
            val message = it.args.first

            it.discord.jda.guilds
                .mapNotNull { it.retrieveOwner().complete() }
                .distinctBy { it.id }
                .forEach {
                    it.user.sendPrivateMessage(message)
                }

            it.reactSuccess()
        }
    }

    command("Transfer") {
        description = "Send an embed to another guild."
        requiredPermissionLevel = Permission.BOT_OWNER
        execute(EmbedArg("Embed").makeNullableOptional { it.guild?.getLoadedEmbed() },
            GuildArg("Target Guild"),
            AnyArg("New Name").makeNullableOptional { null }) {

            val (embed, target, newName) = it.args

            embed ?: return@execute it.respond("No embed selected or loaded.")

            embed.name = newName.takeUnless { it.isNullOrBlank() } ?: embed.name

            val hasCollision = target.getGuildEmbeds().embedList.any { it.name == embed.name }

            if (hasCollision)
                return@execute it.respond("An embed with this name already exists in ${target.name}")

            embedService.addEmbed(target, embed)
            it.reactSuccess()
        }
    }

    command("Guilds") {
        description = "Get a complete list of guilds and info."
        requiredPermissionLevel = Permission.BOT_OWNER
        execute(ChoiceArg("Sort", "name", "size").makeOptional("name")) {
            val guilds = it.discord.jda.guilds
            val sortStyle = it.args.first.toLowerCase()

            val data = when (sortStyle) {
                "name" -> guilds.sortedBy { it.name }
                else -> guilds.sortedBy { -it.getGuildEmbeds().size }
            }.map {
                it.getGuildEmbeds().size to it.name
            }

            val formatter = "%${data.maxOf { it.first.toString().length }}s | %s"

            val report = data.joinToString("\n") { (size, name) ->
                formatter.format(size, name)
            }

            it.respond("Total Guilds: ${guilds.size}\n```$report```")
        }
    }
}