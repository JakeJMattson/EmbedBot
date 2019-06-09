package io.github.jakejmattson.embedbot

import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.api.startBot

private lateinit var commands: CommandsContainer

fun main(args: Array<String>) {
    val token = args.first()

    startBot(token) {
        configure {
            globalPath = "io.github.jakejmattson.embedbot"

            registerInjectionObject(this)
            commands = this@startBot.container
        }

        container.commands.getValue("help").category = "Utility"
    }
}