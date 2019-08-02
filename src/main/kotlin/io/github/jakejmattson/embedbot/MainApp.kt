package io.github.jakejmattson.embedbot

import me.aberrantfox.kjdautils.api.startBot

fun main(args: Array<String>) {
    val token = args.firstOrNull()
            ?: throw IllegalArgumentException("No program arguments provided. Expected bot token.")

    startBot(token) {
        configure {
            globalPath = "io.github.jakejmattson.embedbot"

            registerInjectionObject(this@startBot.container)
            registerInjectionObject(this)
        }
    }
}