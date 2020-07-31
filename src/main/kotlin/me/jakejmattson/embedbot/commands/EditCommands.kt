package me.jakejmattson.embedbot.commands

import me.jakejmattson.discordkt.api.annotations.CommandSet
import me.jakejmattson.discordkt.api.arguments.*
import me.jakejmattson.discordkt.api.dsl.command.commands
import me.jakejmattson.embedbot.arguments.EmbedArg
import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.utils.*
import java.time.LocalDateTime

@CommandSet("Edit")
fun editCommands() = commands {
    command("SetAuthor") {
        description = "Set the author for the currently loaded embed."
        requiresLoadedEmbed = true
        execute(UserArg) {
            val user = it.args.first
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setAuthor(user.name, user.effectiveAvatarUrl)
            it.reactSuccess()
        }
    }

    command("SetColor") {
        description = "Set the color for the currently loaded embed."
        requiresLoadedEmbed = true
        execute(HexColorArg) {
            val color = it.args.first
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setColor(color)
            it.reactSuccess()
        }
    }

    command("SetDescription") {
        description = "Set the description for the currently loaded embed."
        requiresLoadedEmbed = true
        execute(EveryArg) {
            val description = it.args.first
            val embed = it.guild!!.getLoadedEmbed()!!

            if (description.length > DESCRIPTION_LIMIT)
                return@execute it.respond("Max description length is $DESCRIPTION_LIMIT characters. Input was ${description.length}.")

            embed.setDescription(description)
            it.reactSuccess()
        }
    }

    command("SetFooter") {
        description = "Set the footer for the currently loaded embed."
        requiresLoadedEmbed = true
        execute(UrlArg("Icon URL"), EveryArg("Text")) {
            val (url, text) = it.args
            val embed = it.guild!!.getLoadedEmbed()!!

            if (text.length > FOOTER_LIMIT)
                return@execute it.respond("Max footer length is $FOOTER_LIMIT characters. Input was ${text.length}.")

            embed.setFooter(text, url)
            it.reactSuccess()
        }
    }

    command("SetImage") {
        description = "Set the image for the currently loaded embed."
        requiresLoadedEmbed = true
        execute(UrlArg) {
            val (url) = it.args
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setImage(url)
            it.reactSuccess()
        }
    }

    command("SetThumbnail") {
        description = "Set the thumbnail for the currently loaded embed."
        requiresLoadedEmbed = true
        execute(UrlArg) {
            val url = it.args.first
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setThumbnail(url)
            it.reactSuccess()
        }
    }

    command("SetTimestamp") {
        description = "Set the timestamp for the currently loaded embed."
        requiresLoadedEmbed = true
        execute {
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setTimestamp(LocalDateTime.now())
            it.reactSuccess()
        }
    }

    command("SetTitle") {
        description = "Set the title for the currently loaded embed."
        requiresLoadedEmbed = true
        execute(EveryArg) {
            val title = it.args.first
            val embed = it.guild!!.getLoadedEmbed()!!

            if (title.length > TITLE_LIMIT)
                return@execute it.respond("Max title limit is $TITLE_LIMIT characters. Input was ${title.length}.")

            embed.setTitle(title)
            it.reactSuccess()
        }
    }

    command("Clear") {
        description = "Clear a target field from the loaded embed."
        requiresLoadedEmbed = true
        execute(AnyArg("Clear Target").makeOptional("")) {
            val field = it.args.first.toLowerCase()
            val embed = it.guild!!.getLoadedEmbed()!!
            val options = "Options:\nAuthor, Color, Description, Footer, Image, Thumbnail, Timestamp, Title \nAll, Fields, Non-Fields"

            with(embed) {
                when (field) {
                    "author" -> clearAuthor()
                    "color" -> clearColor()
                    "description" -> clearDescription()
                    "footer" -> clearFooter()
                    "image" -> clearImage()
                    "thumbnail" -> clearThumbnail()
                    "timestamp" -> clearTimestamp()
                    "title" -> clearTitle()

                    "all" -> clear()
                    "fields" -> clearFields()
                    "non-fields" -> clearNonFields()
                    else -> null
                }
            } ?: return@execute it.respond("Invalid field selected. $options")

            it.reactSuccess()
        }
    }

    command("Rename") {
        description = "Change the name of an existing embed."
        execute(EmbedArg.makeNullableOptional { it.guild!!.getLoadedEmbed() }, AnyArg("New Name")) {
            val targetEmbed = it.args.first
                ?: return@execute it.respond(messages.MISSING_OPTIONAL_EMBED)

            val newName = it.args.second
            val guild = it.guild!!

            if (guild.hasEmbedWithName(newName))
                return@execute it.respond(messages.EMBED_ALREADY_EXISTS)

            if (targetEmbed.isLoaded(guild)) {
                val updatedEmbed = guild.getEmbedByName(targetEmbed.name)!!
                updatedEmbed.name = newName
                guild.loadEmbed(updatedEmbed)
            } else {
                targetEmbed.name = newName
            }

            it.reactSuccess()
        }
    }
}