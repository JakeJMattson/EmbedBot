package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.services.getLoadedEmbed
import me.aberrantfox.kjdautils.api.dsl.*
import kotlin.streams.toList

@CommandSet("Clear")
fun clearCommands() = commands {
    command("ClearAuthor") {
        requiresGuild = true
        description = "Clear the author from the currently loaded embed."
        execute {
            val embed = getLoadedEmbed(it.guild!!)!!
            embed.clearAuthor()
            it.respond("Author cleared.")
        }
    }

    command("ClearColor") {
        requiresGuild = true
        description = "Clear the color from the currently loaded embed."
        execute {
            val embed = getLoadedEmbed(it.guild!!)!!
            embed.clearColor()
            it.respond("Color cleared.")
        }
    }

    command("ClearDescription") {
        requiresGuild = true
        description = "Clear the description from the currently loaded embed."
        execute {
            val embed = getLoadedEmbed(it.guild!!)!!
            embed.clearDescription()
            it.respond("Description cleared.")
        }
    }

    command("ClearFooter") {
        requiresGuild = true
        description = "Clear the image from the currently loaded embed."
        execute {
            val embed = getLoadedEmbed(it.guild!!)!!
            embed.clearFooter()
            it.respond("Footer cleared.")
        }
    }

    command("ClearImage") {
        requiresGuild = true
        description = "Clear the image from the currently loaded embed."
        execute {
            val embed = getLoadedEmbed(it.guild!!)!!
            embed.clearImage()
            it.respond("Image cleared.")
        }
    }

    command("ClearThumbnail") {
        requiresGuild = true
        description = "Clear the thumbnail from the currently loaded embed."
        execute {
            val embed = getLoadedEmbed(it.guild!!)!!
            embed.clearThumbnail()
            it.respond("Thumbnail cleared.")
        }
    }

    command("ClearTimestamp") {
        requiresGuild = true
        description = "Clear the timestamp from the currently loaded embed."
        execute {
            val embed = getLoadedEmbed(it.guild!!)!!
            embed.clearTimestamp()
            it.respond("Timestamp cleared.")
        }
    }

    command("ClearTitle") {
        requiresGuild = true
        description = "Clear the title of the currently loaded embed."
        execute {
            val embed = getLoadedEmbed(it.guild!!)!!
            embed.clearTitle()
            it.respond("Title cleared.")
        }
    }
}