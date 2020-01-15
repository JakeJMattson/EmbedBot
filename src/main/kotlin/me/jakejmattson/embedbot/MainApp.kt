package me.jakejmattson.embedbot

import me.aberrantfox.kjdautils.api.startBot
import me.jakejmattson.embedbot.locale.messages

lateinit var discordToken: String

fun main(args: Array<String>) {
    discordToken = args.firstOrNull() ?: throw IllegalArgumentException(messages.errors.NO_ARGS)
    startBot(discordToken) { }
}