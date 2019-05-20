package io.github.jakejmattson.embedbot.commands

import com.google.gson.GsonBuilder
import io.github.jakejmattson.embedbot.arguments.EmbedArg
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.*

@CommandSet("Core")
fun coreCommands(embedService: EmbedService) = commands {
    val gson = GsonBuilder().setPrettyPrinting().create()

    command("Send") {
        requiresGuild = true
        description = "Send the currently loaded embed."
        execute {
            val embed = embedService.getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            val builder = embed.builder.takeIf { !it.isEmpty }
                ?:return@execute it.respond("This embed is empty.")

            it.respond(builder.build())
        }
    }

    command("Create") {
        requiresGuild = true
        description = "Create a new embed with this name."
        expect(WordArg)
        execute {
            val embedName = it.args.component1() as String

            embedService.createEmbed(it.guild!!, embedName)

            it.respond("Successfully added the embed: $embedName")
        }
    }

    command("Delete") {
        requiresGuild = true
        description = "Delete the embed with this name."
        expect(arg(EmbedArg, optional = true, default = {embedService.getLoadedEmbed(it.guild!!) as Any}))
        execute {
            val embed = it.args.component1() as Embed

            embedService.removeEmbed(it.guild!!, embed)

            it.respond("Successfully removed the embed: ${embed.name}")
        }
    }

    command("Load") {
        requiresGuild = true
        description = "Load the embed with this name into memory."
        expect(EmbedArg)
        execute {
            val embed = it.args.component1() as Embed

            embedService.loadEmbed(it.guild!!, embed)

            it.respond("Successfully loaded the embed: ${embed.name}")
        }
    }

    command("ListEmbeds") {
        requiresGuild = true
        description = "List all embeds created in this guild."
        execute {
            it.respond(embedService.listEmbeds(it.guild!!))
        }
    }

    command("Import") {
        requiresGuild = true
        description = "Import a JSON String as an embed."
        expect(SentenceArg)
        execute {
            val json = it.args.component1() as String
            val embed = gson.fromJson(json, Embed::class.java) ?: return@execute it.respond("Invalid JSON!")

            embedService.addEmbed(it.guild!!, embed)

            it.respond("Successfully loaded the embed: ${embed.name}")
        }
    }

    command("Export") {
        requiresGuild = true
        description = "Export the currently loaded embed to JSON."
        execute {
            val embed = embedService.getLoadedEmbed(it.guild!!) ?: return@execute it.respond("No embed loaded!")

            it.respond(gson.toJson(embed))
        }
    }
}