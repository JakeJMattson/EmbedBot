package io.github.jakejmattson.embedbot.data

import me.aberrantfox.kjdautils.api.annotation.Data

@Data("config/config.json")
data class Configuration(val botOwner: String = "insert-id",
                         val prefix: String = ">",
                         val guildConfigurations: MutableList<GuildConfiguration> = mutableListOf(GuildConfiguration())) {
    fun getGuildConfig(guildId: String) = guildConfigurations.firstOrNull { it.guildId == guildId }
}

data class GuildConfiguration(val guildId: String = "insert-id",
                              var requiredRole: String = "Staff")