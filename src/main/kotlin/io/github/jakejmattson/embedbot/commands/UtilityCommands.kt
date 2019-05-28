package io.github.jakejmattson.embedbot.commands

import me.aberrantfox.kjdautils.api.dsl.*
import java.awt.Color
import java.util.*

private val startTime = Date()

@CommandSet("Utility")
fun utilityCommands() = commands {
    command("Ping") {
        requiresGuild = true
        description = "Display the network ping of the bot."
        execute {
            it.respond("JDA ping: ${it.jda.ping}ms\n")
        }
    }

    command("Uptime") {
        requiresGuild = true
        description = "Display yhe uptime of the bot."
        execute {
            val milliseconds = Date().time - startTime.time
            val seconds = (milliseconds / 1000) % 60
            val minutes = (milliseconds / (1000 * 60)) % 60
            val hours = (milliseconds / (1000 * 60 * 60)) % 24
            val days = (milliseconds / (1000 * 60 * 60 * 24))

            it.respond(embed {
                setColor(Color.WHITE)
                setTitle("I have been running since")
                setDescription(startTime.toString())

                field {
                    name = "That's been"
                    value = "$days day(s), $hours hour(s), $minutes minute(s) and $seconds second(s)"
                }
            })
        }
    }

    command("ListCommands") {
        requiresGuild = true
        description = "List all available commands."
        execute {
            val commands = it.container.commands.values.groupBy { it.category }.toList()
                .sortedBy { (_, value) -> -value.size }.toMap()

            it.respond(embed {
                commands.forEach {
                    field {
                        name = it.key
                        value = it.value.sortedBy { it.name.length }.joinToString("\n") { it.name }
                        inline = true
                    }
                }
                setColor(Color.green)
            })
        }
    }
}