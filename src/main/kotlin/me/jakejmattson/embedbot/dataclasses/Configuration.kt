package me.jakejmattson.embedbot.dataclasses

import me.jakejmattson.discordkt.api.dsl.data.Data
import me.jakejmattson.discordkt.api.dsl.embed.embed
import me.jakejmattson.discordkt.api.extensions.jda.*
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild

data class Configuration(val botOwner: Long = 254786431656919051,
                         val guildConfigurations: MutableMap<Long, GuildConfiguration> = mutableMapOf()) : Data("config/config.json") {
    operator fun get(id: Long) = guildConfigurations[id]

    fun setup(guild: Guild) {
        if (guildConfigurations[guild.idLong] != null) return

        guildConfigurations[guild.idLong] = GuildConfiguration("=", guild.publicRole.idLong)

        guild.retrieveOwner().queue {
            it.user.sendPrivateMessage(
                embed {
                    simpleTitle = guild.name
                    description = "Thanks for using EmbedBot!"
                    color = infoColor

                    author {
                        val me = guild.jda.retrieveUserById(254786431656919051).complete()

                        name = me.fullName()
                        iconUrl = me.effectiveAvatarUrl
                    }

                    field {
                        name = "Prefix"
                        value = "Use `SetPrefix` to set the prefix that your bot will respond to."
                    }

                    field {
                        name = "Role"
                        value = "Use `SetRole` to set the role that is required to use the bot."
                    }
                }
            )
        }
    }
}

data class GuildConfiguration(var prefix: String,
                              var requiredRoleId: Long) {
    fun getLiveRole(jda: JDA) = jda.getRoleById(requiredRoleId)
}