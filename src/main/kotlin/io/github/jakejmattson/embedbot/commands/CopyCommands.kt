package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.dataclasses.*
import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.services.EmbedService
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.extensions.stdlib.trimToID
import me.aberrantfox.kjdautils.internal.arguments.*
import me.aberrantfox.kjdautils.internal.command.tryRetrieveSnowflake
import net.dv8tion.jda.api.entities.*

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

            val message = tryRetrieveSnowflake(it.discord.jda) {
                channel.retrieveMessageById(messageId.trimToID()).complete()
            } as Message? ?: return@execute it.respond("Could not find a message with that ID in the target channel.")

            val messageEmbed = message.getEmbed()
                ?: return@execute it.respond("This message does not contain an embed.")

            val builder = messageEmbed.toEmbedBuilder()
            val embed = Embed(name, builder, CopyLocation(channel.id, messageId))

            embedService.addEmbed(guild, embed)

            it.reactSuccess()
        }
    }

    command("CopyPrevious") {
        description = "Copy the previous embed in the target channel."
        expect(arg(WordArg("Embed Name")),
                arg(TextChannelArg("Channel"), optional = true, default = { it.channel }))
        execute { event ->
            val name = event.args.component1() as String
            val channel = event.args.component2() as TextChannel
            val guild = event.guild!!
            val limit = 50

            if (guild.hasEmbedWithName(name))
                return@execute event.respond("An embed with this name already exists.")

            val previousMessages = channel.getHistoryBefore(event.message.id, limit).complete().retrievedHistory

            val previousEmbedMessage = previousMessages.firstOrNull { it.getEmbed() != null }
                ?: return@execute event.respond("No embeds found in the previous $limit messages.")

            val builder = previousEmbedMessage.getEmbed()!!.toEmbedBuilder()
            val previousEmbed = Embed(name, builder, CopyLocation(channel.id, previousEmbedMessage.id))

            embedService.addEmbed(guild, previousEmbed)

            event.reactSuccess()
        }
    }

    command("UpdateOriginal") {
        description = "Update the original embed this content was copied from."
        requiresLoadedEmbed = true
        execute {
            val embed = it.guild!!.getLoadedEmbed()!!

            val original = embed.copyLocation
                ?: return@execute it.respond("This embed was not copied from another message.")

            val updateResponse = embed.update(it.discord.jda, original.channelId, original.messageId)

            if (!updateResponse.canUpdate)
                return@execute it.respond(updateResponse.reason)

            it.reactSuccess()
        }
    }

    command("UpdateTarget") {
        description = "Replace the target message embed with the loaded embed."
        requiresLoadedEmbed = true
        expect(arg(TextChannelArg("Channel"), optional = true, default = { it.channel }),
                arg(WordArg("Message ID")))
        execute {
            val channel = it.args.component1() as TextChannel
            val messageId = it.args.component2() as String
            val embed = it.guild!!.getLoadedEmbed()!!

            val updateResponse = embed.update(it.discord.jda, channel.id, messageId)

            if (!updateResponse.canUpdate)
                return@execute it.respond(updateResponse.reason)

            it.reactSuccess()
        }
    }
}