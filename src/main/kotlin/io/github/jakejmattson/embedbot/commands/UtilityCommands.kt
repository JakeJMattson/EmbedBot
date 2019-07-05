package io.github.jakejmattson.embedbot.commands

import com.google.gson.Gson
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.extensions.jda.fullName
import java.awt.Color
import java.util.Date

private data class Properties(val version: String, val author: String, val repository: String)

private val propFile = Properties::class.java.getResource("/properties.json").readText()
private val Project = Gson().fromJson(propFile, Properties::class.java)
private val startTime = Date()

@CommandSet("Utility")
fun utilityCommands() = commands {
    command("Ping") {
        description = "Display the network ping of the bot."
        execute {
            it.respond("Ping: ${it.jda.ping}ms\n")
        }
    }

    command("Source") {
        description = "Display the (source code) repository link."
        execute {
            it.respond(Project.repository)
        }
    }

    command("BotInfo") {
        description = "Display the bot information."
        execute {
            it.respond(embed {
                val self = it.jda.selfUser

                setColor(Color.green)
                setThumbnail(self.effectiveAvatarUrl)
                addField(self.fullName(), "A Discord embed management bot.", true)
                addField("Version", Project.version, true)
                addField("Author", Project.author, true)
                addField("Source", Project.repository, true)
            })
        }
    }

    command("Uptime") {
        description = "Displays how long the bot has been running."
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
        description = "List all available commands."
        execute { event ->
            val commands = event.container.commands.values.groupBy { it.category }.toList()
                .sortedBy { (_, value) -> -value.size }.toMap()

            event.respond(embed {
                commands.forEach {
                    field {
                        name = it.key
                        value = it.value.sortedBy { it.name }.joinToString("\n") { it.name }
                        inline = true
                    }
                }
                setColor(Color.green)
            })
        }
    }
}