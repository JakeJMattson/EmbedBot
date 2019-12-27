package io.github.jakejmattson.embedbot

import io.github.jakejmattson.embedbot.locale.messages
import me.aberrantfox.kjdautils.api.startBot

lateinit var discordToken: String

fun main(args: Array<String>) {
    discordToken = args.firstOrNull() ?: throw IllegalArgumentException(messages.errors.NO_ARGS)
    startBot(discordToken) { }
}