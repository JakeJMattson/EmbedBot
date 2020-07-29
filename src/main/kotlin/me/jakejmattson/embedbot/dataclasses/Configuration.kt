package me.jakejmattson.embedbot.dataclasses

import me.jakejmattson.kutils.api.dsl.data.Data
import net.dv8tion.jda.api.JDA

data class Configuration(val botOwner: String = "",
                         val guildConfigurations: MutableList<GuildConfiguration> = mutableListOf()) : Data("config/config.json") {
    fun getGuildConfig(guildId: String) = guildConfigurations.firstOrNull { it.guildId == guildId }
}

data class GuildConfiguration(val guildId: String,
                              var prefix: String,
                              var requiredRoleId: Long) {
    fun getLiveRole(jda: JDA) = jda.getRoleById(requiredRoleId)
}