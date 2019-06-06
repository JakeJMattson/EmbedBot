package io.github.jakejmattson.embedbot

import io.github.jakejmattson.embedbot.data.Configuration
import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.api.startBot

private lateinit var kjdaConfig: KJDAConfiguration
private lateinit var commands: CommandsContainer

fun main(args: Array<String>) {
    val token = args.first()

    startBot(token) {
        configure {
            globalPath = "io.github.jakejmattson.embedbot"

            kjdaConfig = this
            commands = this@startBot.container
        }

        container.commands.getValue("help").category = "Utility"
    }
}

@Service
class PrefixLoader(configuration: Configuration) { init { kjdaConfig.prefix = configuration.prefix } }