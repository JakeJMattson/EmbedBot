package me.jakejmattson.embedbot.commands

import me.jakejmattson.embedbot.dataclasses.*
import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.locale.messages
import me.jakejmattson.embedbot.services.EmbedService
import me.aberrantfox.kjdautils.api.dsl.command.*
import me.aberrantfox.kjdautils.api.dsl.toEmbedBuilder
import me.aberrantfox.kjdautils.extensions.stdlib.trimToID
import me.aberrantfox.kjdautils.internal.arguments.*
import me.aberrantfox.kjdautils.internal.command.tryRetrieveSnowflake
import net.dv8tion.jda.api.entities.*

@CommandSet("Copy")
fun copyCommands(embedService: EmbedService) = commands {
    command("CopyTarget") {
        description = messages.descriptions.COPY_TARGET
        execute(WordArg("Embed Name"),
            TextChannelArg("Channel").makeOptional { it.channel as TextChannel },
            WordArg("Message ID")) {

            val (name, channel, messageId) = it.args
            val guild = it.guild!!

            if (guild.hasEmbedWithName(name))
                return@execute it.respond(messages.errors.EMBED_ALREADY_EXISTS)

            val message = tryRetrieveSnowflake(it.discord.jda) {
                channel.retrieveMessageById(messageId.trimToID()).complete()
            } as Message? ?: return@execute it.respond(messages.errors.INVALID_MESSAGE_ID)

            val messageEmbed = message.getEmbed()
                ?: return@execute it.respond(messages.errors.NO_EMBED_IN_MESSAGE)

            val builder = messageEmbed.toEmbedBuilder()
            val embed = Embed(name, builder, CopyLocation(channel.id, messageId))

            embedService.addEmbed(guild, embed)

            it.reactSuccess()
        }
    }

    command("UpdateOriginal") {
        description = messages.descriptions.UPDATE_ORIGINAL
        requiresLoadedEmbed = true
        execute {
            val embed = it.guild!!.getLoadedEmbed()!!

            val original = embed.copyLocation
                ?: return@execute it.respond(messages.errors.NOT_COPIED)

            val updateResponse = embed.update(it.discord.jda, original.channelId, original.messageId)

            if (!updateResponse.wasSuccessful)
                return@execute it.respond(updateResponse.message)

            it.reactSuccess()
        }
    }

    command("UpdateTarget") {
        description = messages.descriptions.UPDATE_TARGET
        requiresLoadedEmbed = true
        execute(TextChannelArg("Channel").makeOptional { it.channel as TextChannel }, WordArg("Message ID")) {
            val (channel, messageId) = it.args
            val embed = it.guild!!.getLoadedEmbed()!!
            val updateResponse = embed.update(it.discord.jda, channel.id, messageId)

            if (!updateResponse.wasSuccessful)
                return@execute it.respond(updateResponse.message)

            it.reactSuccess()
        }
    }
}