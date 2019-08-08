package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import java.awt.Color
import java.util.Date

private val startTime = Date()

@CommandSet("Utility")
fun utilityCommands(infoService: InfoService) = commands {

    requiredPermissionLevel = Permission.STAFF

    command("Ping") {
        description = "Display the network ping of the bot."
        execute {
            it.respond("Ping: ${it.discord.jda.gatewayPing}ms\n")
        }
    }

    command("Source") {
        description = "Display the (source code) repository link."
        execute {
            it.respond(infoService.source)
        }
    }

    command("BotInfo") {
        description = "Display the bot information."
        execute {
            it.respond(infoService.botInfo(it.guild!!))
        }
    }

    command("Uptime") {
        description = "Displays how long the bot has been running."
        execute {
            val seconds = (Date().time - startTime.time) / 1000

            it.respondEmbed {
                color = Color.WHITE
                title = "I have been running since"
                description = startTime.toString()

                field {
                    name = "That's been"
                    value = seconds.toMinimalTimeString()
                }
            }
        }
    }

    command("ListCommands") {
        description = "List all available commands."
        execute { event ->
            val commands = event.container.commands.values.groupBy { it.category }.toList()
                .sortedBy { (_, value) -> -value.size }.toMap()

            event.respondEmbed {
                commands.forEach {
                    field {
                        name = it.key
                        value = it.value.sortedBy { it.name }.joinToString("\n") { it.name }
                        inline = true
                    }
                }
                color = Color.green
            }
        }
    }
}