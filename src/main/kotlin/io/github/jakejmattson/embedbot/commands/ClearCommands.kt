package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.dataclasses.Embed
import io.github.jakejmattson.embedbot.extensions.getLoadedEmbed
import me.aberrantfox.kjdautils.api.dsl.*

@CommandSet("Clear")
fun clearCommands() = commands {

    fun generateClearCommand(fieldName: String, embedAction: (embed: Embed) -> Unit) =
        command("Clear$fieldName") {
            requiresGuild = true
            description = "Clear the ${fieldName.toLowerCase()} from the currently loaded embed."
            execute {
                val embed = it.guild!!.getLoadedEmbed()!!
                embedAction.invoke(embed)
                it.respond("$fieldName cleared.")
            }
        }!!

    generateClearCommand("Author") { it.clearAuthor() }
    generateClearCommand("Color") { it.clearColor() }
    generateClearCommand("Description") { it.clearDescription() }
    generateClearCommand("Footer") { it.clearFooter() }
    generateClearCommand("Image") { it.clearImage() }
    generateClearCommand("Thumbnail") { it.clearThumbnail() }
    generateClearCommand("Timestamp") { it.clearTimestamp() }
    generateClearCommand("Title") { it.clearTitle() }
}