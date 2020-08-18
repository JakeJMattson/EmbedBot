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

        val everyone = guild.publicRole
        val newConfiguration = GuildConfiguration("embed!", everyone.idLong)

        guildConfigurations[guild.idLong] = newConfiguration
        save()

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

                    val prefix = newConfiguration.prefix

                    field {
                        name = "Prefix"
                        value = "This is the prefix used before commands given to the bot.\n" +
                            "Default: $prefix\n" +
                            "Use `${prefix}SetPrefix` to change this."
                    }

                    field {
                        name = "Role"
                        value = "This is the role that a user must have to use this bot.\n" +
                            "Default: ${everyone.asMention}\n" +
                            "Use `${prefix}SetRole` to change this."
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