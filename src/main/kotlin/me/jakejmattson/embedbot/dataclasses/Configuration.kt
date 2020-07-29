package me.jakejmattson.embedbot.dataclasses

import me.jakejmattson.kutils.api.dsl.data.Data

data class Configuration(val botOwner: String = "",
                         val guildConfigurations: MutableList<GuildConfiguration> = mutableListOf(GuildConfiguration())) : Data("config/config.json") {
    fun getGuildConfig(guildId: String) = guildConfigurations.firstOrNull { it.guildId == guildId }
}

data class GuildConfiguration(val guildId: String = "insert-id",
                              var prefix: String = "",
                              var requiredRole: String = "Staff")