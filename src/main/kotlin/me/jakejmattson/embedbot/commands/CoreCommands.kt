package me.jakejmattson.embedbot.commands

import com.google.gson.JsonSyntaxException
import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.internal.arguments.*
import me.jakejmattson.embedbot.arguments.EmbedArg
import me.jakejmattson.embedbot.dataclasses.CopyLocation
import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.locale.messages
import me.jakejmattson.embedbot.services.*
import net.dv8tion.jda.api.entities.TextChannel

@CommandSet("Core")
fun coreCommands(embedService: EmbedService) = commands {
    command("Send") {
        description = messages.descriptions.SEND
        requiresLoadedEmbed = true
        execute(TextChannelArg("Channel").makeOptional { it.channel as TextChannel },
            BooleanArg("shouldTrack").makeOptional(false)) {
            val (channel, shouldTrack) = it.args
            val embed = it.guild!!.getLoadedEmbed()!!

            if (embed.isEmpty)
                return@execute it.respond(messages.errors.EMPTY_EMBED)

            channel.sendMessage(embed.build()).queue { message ->
                if (shouldTrack)
                    embed.copyLocation = CopyLocation(channel.id, message.id)
            }

            if (channel != it.channel)
                it.reactSuccess()
        }
    }

    command("Create") {
        description = messages.descriptions.CREATE
        execute(WordArg("Embed Name")) {
            val embedName = it.args.first
            val wasCreated = embedService.createEmbed(it.guild!!, embedName)

            if (!wasCreated)
                return@execute it.respond(messages.errors.EMBED_ALREADY_EXISTS)

            it.reactSuccess()
        }
    }

    command("Duplicate") {
        description = messages.descriptions.DUPLICATE
        execute(WordArg("Embed Name"), EmbedArg.makeNullableOptional { it.guild!!.getLoadedEmbed() }) {
            val embedName = it.args.first
            val existingEmbed = it.args.second
                ?: return@execute it.respond(messages.errors.MISSING_OPTIONAL_EMBED)

            val embed = createEmbedFromJson(embedName, existingEmbed.toJson())
            val wasCreated = embedService.addEmbed(it.guild!!, embed)

            if (!wasCreated)
                it.respond(messages.errors.EMBED_ALREADY_EXISTS)

            it.reactSuccess()
        }
    }

    command("Delete") {
        description = messages.descriptions.DELETE

        execute(EmbedArg.makeNullableOptional { it.guild!!.getLoadedEmbed() }) {
            val embed = it.args.first
                ?: return@execute it.respond(messages.errors.MISSING_OPTIONAL_EMBED)

            it.guild!!.removeEmbed(embed)

            it.reactSuccess()
        }
    }

    command("Load") {
        description = messages.descriptions.LOAD
        execute(EmbedArg) {
            val embed = it.args.first
            it.guild!!.loadEmbed(embed)
            it.reactSuccess()
        }
    }

    command("Import") {
        description = messages.descriptions.IMPORT
        execute(WordArg("Embed Name"), SentenceArg("JSON")) {
            val (name, json) = it.args
            val guild = it.guild!!

            if (guild.hasEmbedWithName(name))
                return@execute it.respond(messages.errors.EMBED_ALREADY_EXISTS)

            try {
                val embed = createEmbedFromJson(name, json)
                val wasAdded = embedService.addEmbed(guild, embed)

                if (!wasAdded)
                    it.respond(messages.errors.EMBED_ALREADY_EXISTS)

                it.reactSuccess()
            } catch (e: JsonSyntaxException) {
                it.respond("Invalid JSON! ${e.message?.substringAfter("Exception: ")}")
            }
        }
    }

    command("Export") {
        description = messages.descriptions.EXPORT
        execute(EmbedArg.makeNullableOptional { it.guild!!.getLoadedEmbed() }) {
            val embed = it.args.first
                ?: return@execute it.respond(messages.errors.MISSING_OPTIONAL_EMBED)

            val json = embed.toJson()

            if (json.length >= 1985)
                it.channel.sendFile(json.toByteArray(), "$${embed.name}.json").queue()
            else
                it.respond("```json\n$json```")
        }
    }
}