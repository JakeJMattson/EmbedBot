package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.extensions.respondEmbed
import io.github.jakejmattson.embedbot.locale.messages
import me.aberrantfox.kjdautils.api.dsl.command.*
import me.aberrantfox.kjdautils.extensions.stdlib.toMinimalTimeString
import java.util.Date

private val startTime = Date()

@CommandSet("Utility")
fun utilityCommands() = commands {
    command("Ping") {
        description = messages.descriptions.PING
        execute { event ->
            event.discord.jda.restPing.queue {
                event.respond("Ping: ${it}ms\n")
            }
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
}