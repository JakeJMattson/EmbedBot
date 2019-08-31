package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.locale.messages
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.extensions.jda.toMember
import java.util.Date

private val startTime = Date()

@CommandSet("Utility")
fun utilityCommands(infoService: InfoService, permissionsService: PermissionsService) = commands {
    command("Ping") {
        description = messages.descriptions.PING
        execute {
            it.respond("Ping: ${it.discord.jda.gatewayPing}ms\n")
        }
    }

    command("BotInfo") {
        description = messages.descriptions.BOT_INFO
        execute {
            it.respond(infoService.botInfo(it.guild!!))
        }
    }

    command("Uptime") {
        description = messages.descriptions.UPTIME
        execute {
            val seconds = (Date().time - startTime.time) / 1000

            it.respondEmbed {
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
        description = messages.descriptions.LIST_COMMANDS
        execute { event ->
            val member = event.author.toMember(event.guild!!)!!

            val commands = event.container.commands.values.asSequence()
                .filter { permissionsService.hasClearance(member, it.requiredPermissionLevel) }
                .groupBy { it.category }.toList()
                .filter { it.second.isNotEmpty() }
                .sortedBy { (_, value) -> -value.size }
                .toList().toMap()

            event.respondEmbed {
                commands.forEach { entry ->
                    addInlineField(entry.key, entry.value.sortedBy { it.name }.joinToString("\n") { it.name })
                }
            }
        }
    }
}