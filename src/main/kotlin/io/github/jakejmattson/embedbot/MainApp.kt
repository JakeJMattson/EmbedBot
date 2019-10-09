package io.github.jakejmattson.embedbot

import io.github.jakejmattson.embedbot.locale.messages
import me.aberrantfox.kjdautils.api.startBot

fun main(args: Array<String>) {
    val token = args.firstOrNull() ?: throw IllegalArgumentException(messages.errors.NO_ARGS)
    startBot(token) { }
}