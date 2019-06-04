package io.github.jakejmattson.embedbot

import io.github.jakejmattson.embedbot.utilities.generateDocs
import me.aberrantfox.kjdautils.api.dsl.CommandsContainer
import me.aberrantfox.kjdautils.api.startBot

private lateinit var commands: CommandsContainer

fun main(args: Array<String>) {
    val token = args.first()

    startBot(token) {
        configure {
            prefix = ">"
            globalPath = "io.github.jakejmattson.embedbot"

            commands = this@startBot.container
        }

        container.commands.getValue("help").category = "Utility"
    }

    println(generateDocs(commands))
}