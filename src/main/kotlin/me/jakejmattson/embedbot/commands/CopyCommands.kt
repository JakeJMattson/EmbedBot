package me.jakejmattson.embedbot.commands

import me.jakejmattson.discordkt.api.annotations.CommandSet
import me.jakejmattson.discordkt.api.arguments.*
import me.jakejmattson.discordkt.api.dsl.command.commands
import me.jakejmattson.discordkt.api.dsl.embed.toEmbedBuilder
import me.jakejmattson.discordkt.api.extensions.stdlib.trimToID
import me.jakejmattson.embedbot.dataclasses.*
import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.services.EmbedService
import me.jakejmattson.embedbot.utils.messages
import net.dv8tion.jda.api.entities.TextChannel

@CommandSet("Copy")
fun copyCommands(embedService: EmbedService) = commands {
    command("CopyTarget") {
        description = "Copy an embed by its message ID."
        execute(AnyArg("Embed Name"),
            TextChannelArg("Channel").makeOptional { it.channel as TextChannel },
            AnyArg("Message ID")) {

            val (name, channel, messageId) = it.args
            val guild = it.guild!!

            if (guild.hasEmbedWithName(name))
                return@execute it.respond(messages.EMBED_ALREADY_EXISTS)

            val message = it.discord.retrieveEntity {
                channel.retrieveMessageById(messageId.trimToID()).complete()
            } ?: return@execute it.respond("Could not find a message with that ID in the target channel.")

            val messageEmbed = message.getEmbed()
                ?: return@execute it.respond("Target message has no embed.")

            val builder = messageEmbed.toEmbedBuilder()
            val embed = Embed(name, builder, CopyLocation(channel.id, messageId))

            embedService.addEmbed(guild, embed)

            it.reactSuccess()
        }
    }

    command("UpdateOriginal") {
        description = "Update the original embed this content was copied from."
        requiresLoadedEmbed = true
        execute {
            val embed = it.guild!!.getLoadedEmbed()!!

            val original = embed.copyLocation
                ?: return@execute it.respond(messages.NOT_COPIED)

            val updateResponse = embed.update(it.discord, original.channelId, original.messageId)

            if (!updateResponse.wasSuccessful)
                return@execute it.respond(updateResponse.message)

            it.reactSuccess()
        }
    }

    command("UpdateTarget") {
        description = "Replace the target message embed with the loaded embed."
        requiresLoadedEmbed = true
        execute(TextChannelArg("Channel").makeOptional { it.channel as TextChannel }, AnyArg("Message ID")) {
            val (channel, messageId) = it.args
            val embed = it.guild!!.getLoadedEmbed()!!
            val updateResponse = embed.update(it.discord, channel.id, messageId)

            if (!updateResponse.wasSuccessful)
                return@execute it.respond(updateResponse.message)

            it.reactSuccess()
        }
    }
}