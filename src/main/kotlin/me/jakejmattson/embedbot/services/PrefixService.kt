package me.jakejmattson.embedbot.services

import me.jakejmattson.embedbot.dataclasses.Configuration
import me.jakejmattson.kutils.api.Discord
import me.jakejmattson.kutils.api.annotations.Service

@Service
class PrefixService(private val configuration: Configuration, private val discord: Discord) {
    fun setPrefix(prefix: String) {
        configuration.prefix = prefix
        discord.configuration.prefix { prefix }
    }
}