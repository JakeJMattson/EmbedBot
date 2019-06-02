package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.services.getLoadedEmbed
import me.aberrantfox.kjdautils.api.dsl.*
import kotlin.streams.toList

@CommandSet("Clear")
fun clearCommands() = commands {
    command("ClearTitle") {
        requiresGuild = true
        description = "Clear the title of the currently loaded embed."
        execute {
            val embed = getLoadedEmbed(it.guild!!)!!
            embed.clearTitle()
            it.respond("Title cleared.")
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

    command("ClearThumbnail") {
        requiresGuild = true
        description = "Clear the thumbnail from the currently loaded embed."
        execute {
            val embed = getLoadedEmbed(it.guild!!)!!
            embed.clearThumbnail()
            it.respond("Thumbnail cleared.")
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

    command("ClearFields") {
        requiresGuild = true
        description = "Clear all fields from the currently loaded embed."
        execute {
            val embed = getLoadedEmbed(it.guild!!)!!
            embed.clearFields()
            it.respond("Fields cleared.")
        }
    }

    command("ClearNonFields") {
        requiresGuild = true
        description = "Clear all non-field entities from the currently loaded embed."
        execute {
            val embed = getLoadedEmbed(it.guild!!)!!
            val fields = embed.fields.stream().toList()

            embed.clear()
            embed.setFields(fields)

            it.respond("Non-fields cleared.")
        }
    }

    command("Clear") {
        requiresGuild = true
        description = "Clear the currently loaded embed."
        execute {
            val embed = getLoadedEmbed(it.guild!!)!!
            embed.clear()
            it.respond("Embed cleared.")
        }
    }
}