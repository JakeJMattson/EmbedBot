package me.jakejmattson.embedbot.dataclasses

import me.jakejmattson.kutils.api.annotations.Data

@Data("config/config.json")
data class Configuration(val botOwner: String = "",
                         var prefix: String = "",
                         val githubToken: String = "",
                         val guildConfigurations: MutableList<GuildConfiguration> = mutableListOf(GuildConfiguration())) {
    fun getGuildConfig(guildId: String) = guildConfigurations.firstOrNull { it.guildId == guildId }
}

data class GuildConfiguration(val guildId: String = "insert-id",
                              var requiredRole: String = "Staff")