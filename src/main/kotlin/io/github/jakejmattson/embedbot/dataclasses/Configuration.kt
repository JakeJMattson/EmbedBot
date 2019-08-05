package io.github.jakejmattson.embedbot.dataclasses

import me.aberrantfox.kjdautils.api.annotation.Data

@Data("config/config.json")
data class Configuration(val botOwner: String = "insert-id",
                         var prefix: String = "=",
                         val guildConfigurations: MutableList<GuildConfiguration> = mutableListOf(GuildConfiguration())) {
    fun getGuildConfig(guildId: String) = guildConfigurations.firstOrNull { it.guildId == guildId }
}

data class GuildConfiguration(val guildId: String = "insert-id",
                              var requiredRole: String = "Staff")