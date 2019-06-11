package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.extensions.getLoadedEmbed
import me.aberrantfox.kjdautils.api.dsl.*
import kotlin.streams.toList

@CommandSet("ClearGroup")
fun clearGroupCommands() = commands {
    command("ClearFields") {
        requiresGuild = true
        description = "Clear all fields from the currently loaded embed."
        execute {
            val embed = it.guild!!.getLoadedEmbed()!!
            embed.clearFields()
            it.respond("Fields cleared.")
        }
    }

    command("ClearNonFields") {
        requiresGuild = true
        description = "Clear all non-field entities from the currently loaded embed."
        execute {
            val embed = it.guild!!.getLoadedEmbed()!!
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
            val embed = it.guild!!.getLoadedEmbed()!!
            embed.clear()
            it.respond("Embed cleared.")
        }
    }
}