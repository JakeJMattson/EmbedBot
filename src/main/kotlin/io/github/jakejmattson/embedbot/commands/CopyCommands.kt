package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.dataclasses.*
import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.services.EmbedService
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.extensions.stdlib.trimToID
import me.aberrantfox.kjdautils.internal.command.arguments.*
import me.aberrantfox.kjdautils.internal.command.tryRetrieveSnowflake
import net.dv8tion.jda.core.entities.*

@CommandSet("Copy")
fun copyCommands(embedService: EmbedService) = commands {
    command("CopyTarget") {
        description = "Copy an embed by its message ID."
        expect(arg(WordArg("Embed Name")),
                arg(TextChannelArg("Channel"), optional = true, default = { it.channel }),
                arg(WordArg("Message ID")))
        execute {
            val name = it.args.component1() as String
            val channel = it.args.component2() as TextChannel
            val messageId = it.args.component3() as String
            val guild = it.guild!!

            if (guild.hasEmbedWithName(name))
                return@execute it.respond("An embed with this name already exists.")

            val message = tryRetrieveSnowflake(it.jda) {
                channel.getMessageById(messageId.trimToID()).complete()
            } as Message? ?: return@execute it.respond("Could not find a message with that ID in the target channel.")

            val messageEmbed = message.getEmbed()
                ?: return@execute it.respond("This message does not contain an embed.")

            val builder = messageEmbed.toEmbedBuilder()
            val embed = Embed(name, builder, CopyLocation(channel.id, messageId))
            embedService.addEmbed(guild, embed)
            it.respond("Successfully copied the embed as: ${embed.name}")
        }
    }

    command("CopyPrevious") {
        description = "Copy the previous embed in the target channel."
        expect(arg(WordArg("Embed Name")),
                arg(TextChannelArg("Channel"), optional = true, default = { it.channel }))
        execute {
            val name = it.args.component1() as String
            val channel = it.args.component2() as TextChannel
            val guild = it.guild!!
            val limit = 50

            if (guild.hasEmbedWithName(name))
                return@execute it.respond("An embed with this name already exists.")

            val previousMessages = channel.getHistoryBefore(it.message.id, limit).complete().retrievedHistory

            val previousEmbedMessage = previousMessages.firstOrNull { it.getEmbed() != null }
                ?: return@execute it.respond("No embeds found in the previous $limit messages.")

            val builder = previousEmbedMessage.getEmbed()!!.toEmbedBuilder()
            val previousEmbed = Embed(name, builder, CopyLocation(channel.id, previousEmbedMessage.id))
            embedService.addEmbed(guild, previousEmbed)
            it.respond("Successfully copied the embed as: ${previousEmbed.name}")
        }
    }

    command("UpdateOriginal") {
        description = "Update the original embed this content was copied from."
        execute {
            val embed = it.guild!!.getLoadedEmbed()
                ?: return@execute it.respond("No embed loaded!")

            val updateResponse = embed.update(it.jda)

            if (!updateResponse.canUpdate)
                return@execute it.respond(updateResponse.reason)

            it.respond("Message updated!")
        }
    }
}