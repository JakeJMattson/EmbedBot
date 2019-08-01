package io.github.jakejmattson.embedbot.commands

import com.google.gson.JsonSyntaxException
import io.github.jakejmattson.embedbot.arguments.*
import io.github.jakejmattson.embedbot.dataclasses.*
import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.services.EmbedService
import io.github.jakejmattson.embedbot.utilities.createEmbedFromJson
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.*
import net.dv8tion.jda.core.entities.TextChannel

@CommandSet("Core")
fun coreCommands(embedService: EmbedService) = commands {
    command("Send") {
        description = "Send the currently loaded embed."
        requiresLoadedEmbed = true
        expect(arg(TextChannelArg("Channel"), optional = true, default = { it.channel }),
            arg(BooleanArg("shouldTrack"), optional = true, default = false))
        execute {
            val channel = it.args.component1() as TextChannel
            val shouldTrack = it.args.component2() as Boolean

            val embed = it.guild!!.getLoadedEmbed()!!

            if (embed.isEmpty)
                return@execute it.respond("This embed is empty.")

            channel.sendMessage(embed.build()).queue { message ->
                if (shouldTrack)
                    embed.copyLocation = CopyLocation(channel.id, message.id)
            }
        }
    }

    command("Create") {
        description = "Create a new embed with this name."
        expect(WordArg("Embed Name"))
        execute {
            val embedName = it.args.component1() as String
            val wasCreated = embedService.createEmbed(it.guild!!, embedName)

            it.respond(
                if (wasCreated)
                    "Successfully added the embed: $embedName"
                else
                    "An embed with this name already exists."
            )
        }
    }

    command("Duplicate") {
        description = "Create a new embed from an existing embed."
        expect(WordArg("Embed Name"), EmbedArg)
        execute {
            val embedName = it.args.component1() as String
            val existingEmbed = it.args.component2() as Embed
            val embed = createEmbedFromJson(embedName, existingEmbed.toJson())
            val wasCreated = embedService.addEmbed(it.guild!!, embed)

            it.respond(
                if (wasCreated)
                    "Successfully added the embed: $embedName"
                else
                    "An embed with this name already exists."
            )
        }
    }

    command("Delete") {
        description = "Delete the embed with this name."
        requiresLoadedEmbed = true
        expect(arg(EmbedArg, optional = true, default = { it.guild!!.getLoadedEmbed() as Any }))
        execute {
            val embed = it.args.component1() as Embed
            it.guild!!.removeEmbed(embed)
            it.respond("Successfully removed the embed: ${embed.name}")
        }
    }

    command("Load") {
        description = "Load the embed with this name into memory."
        expect(EmbedArg)
        execute {
            val embed = it.args.component1() as Embed
            it.guild!!.loadEmbed(embed)
            it.respond("Successfully loaded the embed: ${embed.name}")
        }
    }

    command("Import") {
        description = "Import a JSON String as an embed."
        expect(WordArg("Embed Name"), SentenceArg("JSON"))
        execute {
            val name = it.args.component1() as String
            val json = it.args.component2() as String
            val guild = it.guild!!

            if (guild.hasEmbedWithName(name))
                return@execute it.respond("An embed with this name already exists.")

            it.respond(
                try {
                    val embed = createEmbedFromJson(name, json)
                    val wasAdded = embedService.addEmbed(guild, embed)

                    if (wasAdded) "Successfully imported the embed: ${embed.name}" else "An embed with this name already exists"
                } catch (e: JsonSyntaxException) {
                    "Invalid JSON! ${e.message?.substringAfter("Exception: ")}"
                }
            )
        }
    }

    command("Export") {
        description = "Export the currently loaded embed to JSON."
        requiresLoadedEmbed = true
        execute {
            val embed = it.guild!!.getLoadedEmbed()!!
            val json = embed.toJson()

            if (json.length >= 1985)
                it.channel.sendFile(json.toByteArray(), "$${embed.name}.json").queue()
            else
                it.respond("```json\n$json```")
        }
    }
}