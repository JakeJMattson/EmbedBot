package io.github.jakejmattson.embedbot.commands

import com.google.gson.JsonSyntaxException
import io.github.jakejmattson.embedbot.arguments.EmbedArg
import io.github.jakejmattson.embedbot.dataclasses.Embed
import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.services.EmbedService
import io.github.jakejmattson.embedbot.utilities.createEmbedFromJson
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.*

@CommandSet("Core")
fun coreCommands(embedService: EmbedService) = commands {
    command("Send") {
        requiresGuild = true
        description = "Send the currently loaded embed."
        execute {
            val embed = it.guild!!.getLoadedEmbed()
                ?: return@execute it.respond("No embed loaded!")

            if (embed.isEmpty)
                return@execute it.respond("This embed is empty.")

            it.respond(embed.build())
        }
    }

    command("Create") {
        requiresGuild = true
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

    command("Delete") {
        requiresGuild = true
        description = "Delete the embed with this name."
        expect(arg(EmbedArg, optional = true, default = { it.guild!!.getLoadedEmbed() as Any }))
        execute {
            val embed = it.args.component1() as Embed
            val wasRemoved = embedService.removeEmbed(it.guild!!, embed)

            it.respond(
                if (wasRemoved)
                    "Successfully removed the embed: ${embed.name}"
                else
                    "No such embed exists"
            )
        }
    }

    command("Load") {
        requiresGuild = true
        description = "Load the embed with this name into memory."
        expect(EmbedArg)
        execute {
            val embed = it.args.component1() as Embed
            it.guild!!.loadEmbed(embed)
            it.respond("Successfully loaded the embed: ${embed.name}")
        }
    }

    command("ListEmbeds") {
        requiresGuild = true
        description = "List all embeds created in this guild."
        execute {
            it.respond(
                embed {
                    val embeds = it.guild!!.listEmbeds().takeIf { it.isNotEmpty() } ?: "<No embeds>"
                    val clusters = it.guild!!.listClusters().takeIf { it.isNotEmpty() } ?: "<No clusters>"

                    addField("Embeds", embeds, false)
                    addField("Clusters", clusters, false)
                }
            )
        }
    }

    command("Import") {
        requiresGuild = true
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
        requiresGuild = true
        description = "Export the currently loaded embed to JSON."
        execute {
            val embed = it.guild!!.getLoadedEmbed()
                ?: return@execute it.respond("No embed loaded!")

            it.respond(embed.toJson())
        }
    }
}