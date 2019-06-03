package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.HexColorArg
import io.github.jakejmattson.embedbot.services.getLoadedEmbed
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.*
import java.time.LocalDateTime

private const val editFormat = "Successfully updated the embed %s!"

@CommandSet("Edit")
fun editCommands() = commands {
    command("SetAuthor") {
        requiresGuild = true
        description = "Set the author for the currently loaded embed."
        expect(SentenceArg)
        execute {
            val description = it.args.component1() as String
            val embed = getLoadedEmbed(it.guild!!)!!

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
            val embed = getLoadedEmbed(it.guild!!)!!

            embed.setColor(color)
            it.respond(editFormat.format("color"))
        }
    }

    command("SetDescription") {
        requiresGuild = true
        description = "Set the description for the currently loaded embed."
        expect(SentenceArg)
        execute {
            val description = it.args.component1() as String
            val embed = getLoadedEmbed(it.guild!!)!!

            embed.setDescription(description)
            it.respond(editFormat.format("description"))
        }
    }

    command("SetFooter") {
        requiresGuild = true
        description = "Set the footer for the currently loaded embed."
        expect(UrlArg("Icon URL"), SentenceArg("Text"))
        execute {
            val url = it.args.component1() as String
            val text = it.args.component2() as String
            val embed = getLoadedEmbed(it.guild!!)!!

            embed.setFooter(text, url)
            it.respond(editFormat.format("footer"))
        }
    }

    command("SetImage") {
        requiresGuild = true
        description = "Set the image for the currently loaded embed."
        expect(UrlArg)
        execute {
            val url = it.args.component1() as String
            val embed = getLoadedEmbed(it.guild!!)!!

            embed.setImage(url)
            it.respond(editFormat.format("image"))
        }
    }

    command("SetThumbnail") {
        requiresGuild = true
        description = "Set the thumbnail for the currently loaded embed."
        expect(UrlArg)
        execute {
            val url = it.args.component1() as String
            val embed = getLoadedEmbed(it.guild!!)!!

            embed.setThumbnail(url)
            it.respond(editFormat.format("thumbnail"))
        }
    }

    command("SetTimestamp") {
        requiresGuild = true
        description = "Set the timestamp for the currently loaded embed."
        execute {
            val embed = getLoadedEmbed(it.guild!!)!!
            embed.setTimestamp(LocalDateTime.now())
            it.respond(editFormat.format("timestamp"))
        }
    }

    command("SetTitle") {
        requiresGuild = true
        description = "Set the title for the currently loaded embed."
        expect(SentenceArg)
        execute {
            val title = it.args.component1() as String
            val embed = getLoadedEmbed(it.guild!!)!!

            embed.setTitle(title)
            it.respond(editFormat.format("title"))
        }
    }
}