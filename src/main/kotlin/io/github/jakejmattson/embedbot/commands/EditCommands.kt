package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.HexColorArg
import io.github.jakejmattson.embedbot.extensions.getLoadedEmbed
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.*
import java.time.LocalDateTime

private const val commandResponseFormat = "Successfully updated the embed %s!"
private const val commandDescriptionFormat = "Set the %s for the currently loaded embed."

@CommandSet("Edit")
fun editCommands() = commands {
    command("SetAuthor") {
        requiresGuild = true
        description = commandDescriptionFormat.format("author")
        expect(SentenceArg)
        execute {
            val description = it.args.component1() as String
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setAuthor(description)
            it.respond(commandResponseFormat.format("author"))
        }
    }

    command("SetColor") {
        requiresGuild = true
        description = commandDescriptionFormat.format("color")
        expect(HexColorArg)
        execute {
            val color = it.args.component1() as Int
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setColor(color)
            it.respond(commandResponseFormat.format("color"))
        }
    }

    command("SetDescription") {
        requiresGuild = true
        description = commandDescriptionFormat.format("description")
        expect(SentenceArg)
        execute {
            val description = it.args.component1() as String
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setDescription(description)
            it.respond(commandResponseFormat.format("description"))
        }
    }

    command("SetFooter") {
        requiresGuild = true
        description = commandDescriptionFormat.format("footer")
        expect(UrlArg("Icon URL"), SentenceArg("Text"))
        execute {
            val url = it.args.component1() as String
            val text = it.args.component2() as String
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setFooter(text, url)
            it.respond(commandResponseFormat.format("footer"))
        }
    }

    command("SetImage") {
        requiresGuild = true
        description = commandDescriptionFormat.format("image")
        expect(UrlArg)
        execute {
            val url = it.args.component1() as String
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setImage(url)
            it.respond(commandResponseFormat.format("image"))
        }
    }

    command("SetThumbnail") {
        requiresGuild = true
        description = commandDescriptionFormat.format("thumbnail")
        expect(UrlArg)
        execute {
            val url = it.args.component1() as String
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setThumbnail(url)
            it.respond(commandResponseFormat.format("thumbnail"))
        }
    }

    command("SetTimestamp") {
        requiresGuild = true
        description = commandDescriptionFormat.format("timestamp")
        execute {
            val embed = it.guild!!.getLoadedEmbed()!!
            embed.setTimestamp(LocalDateTime.now())
            it.respond(commandResponseFormat.format("timestamp"))
        }
    }

    command("SetTitle") {
        requiresGuild = true
        description = commandDescriptionFormat.format("title")
        expect(SentenceArg)
        execute {
            val title = it.args.component1() as String
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setTitle(title)
            it.respond(commandResponseFormat.format("title"))
        }
    }
}