package me.jakejmattson.embedbot.commands

import me.jakejmattson.embedbot.locale.messages
import me.jakejmattson.kutils.api.annotations.CommandSet
import me.jakejmattson.kutils.api.dsl.command.commands
import me.jakejmattson.kutils.api.extensions.stdlib.toTimeString
import java.awt.Color
import java.util.Date

private val startTime = Date()

@CommandSet("Utility")
fun utilityCommands() = commands {
    command("Status", "Ping", "Uptime") {
        description = messages.descriptions.STATUS
        execute { event ->
            val jda = event.discord.jda

            jda.restPing.queue { restPing ->
                event.respond {
                    color = Color(0x00bfff)

                    val seconds = (Date().time - startTime.time) / 1000

                    addField("Rest ping", "${restPing}ms")
                    addField("Gateway ping", "${jda.gatewayPing}ms")
                    addField("Total Uptime", seconds.toTimeString())
                }
            }
        }
    }
}