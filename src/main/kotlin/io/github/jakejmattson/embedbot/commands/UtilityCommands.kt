package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.extensions.jda.toMember
import java.awt.Color
import java.util.Date

private val startTime = Date()

@CommandSet("Utility")
fun utilityCommands(infoService: InfoService, permissionsService: PermissionsService) = commands {
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
            val member = event.author.toMember(event.guild!!)!!

            val commands = event.container.commands.values.asSequence()
                .filter { permissionsService.hasClearance(member, it.requiredPermissionLevel) }
                .groupBy { it.category }.toList()
                .filter { it.second.isNotEmpty() }
                .sortedBy { (_, value) -> -value.size }
                .toList().toMap()

            event.respondEmbed {
                commands.forEach {
                    addInlineField(it.key, it.value.sortedBy { it.name }.joinToString("\n") { it.name })
                }
                color = Color.green
            }
        }
    }
}