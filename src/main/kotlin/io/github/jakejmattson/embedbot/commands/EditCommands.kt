package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.HexColorArg
import io.github.jakejmattson.embedbot.services.getLoadedEmbed
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.*

private const val editFormat = "Successfully updated the embed %s!"

@CommandSet("Edit")
fun editCommands() = commands {
    command("SetTitle") {
        requiresGuild = true
        description = "Set the title for the currently loaded embed."
        expect(SentenceArg)
        execute {
            val title = it.args.component1() as String

            val embed = getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            embed.setTitle(title)
            it.respond(editFormat.format("title"))
        }
    }

    command("SetDescription") {
        requiresGuild = true
        description = "Set the description for the currently loaded embed."
        expect(SentenceArg)
        execute {
            val description = it.args.component1() as String

            val embed = getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            embed.setDescription(description)
            it.respond(editFormat.format("description"))
        }
    }

    command("SetAuthor") {
        requiresGuild = true
        description = "Set the author for the currently loaded embed."
        expect(SentenceArg)
        execute {
            val description = it.args.component1() as String

            val embed = getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            embed.setAuthor(description)
            it.respond(editFormat.format("author"))
        }
    }

    command("SetColor") {
        requiresGuild = true
        description = "Set the color for the currently loaded embed."
        expect(HexColorArg)
        execute {
            val color = it.args.component1() as Int

            val embed = getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            embed.setColor(color)
            it.respond(editFormat.format("color"))
        }
    }

    command("SetThumbnail") {
        requiresGuild = true
        description = "Set the thumbnail for the currently loaded embed."
        expect(UrlArg)
        execute {
            val url = it.args.component1() as String

            val embed = getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            embed.setThumbnail(url)
            it.respond(editFormat.format("thumbnail"))
        }
    }
}