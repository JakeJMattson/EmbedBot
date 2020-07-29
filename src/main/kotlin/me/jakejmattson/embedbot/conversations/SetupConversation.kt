package me.jakejmattson.embedbot.conversations

import me.jakejmattson.embedbot.dataclasses.*
import me.jakejmattson.kutils.api.arguments.*
import me.jakejmattson.kutils.api.dsl.conversation.*
import me.jakejmattson.kutils.api.extensions.jda.fullName
import net.dv8tion.jda.api.entities.Guild

class SetupConversation(private val config: Configuration) : Conversation() {
    @Start
    fun start(guild: Guild) = conversation("cancel") {
        respond {
            author {
                val jda = discord.jda
                val me = jda.retrieveUserById(254786431656919051).complete()

                name = me.fullName()
                iconUrl = me.effectiveAvatarUrl
            }

            simpleTitle = guild.name
            description = "Thanks for using EmbedBot! This bot will now walk you through the setup procedure. You can type `cancel` at any time to cancel this setup."
            color = infoColor
        }

        val prefix = promptEmbed(AnyArg) {
            simpleTitle = "Prefix"
            description = "Please enter the prefix that the bot will respond to. You can change this later with the `SetPrefix` command."
            color = infoColor
        }

        val role = promptEmbed(RoleArg(guildId = guild.id)) {
            simpleTitle = "Role"
            description = "Please enter the role required to use the bot on this server. You can change this later with the `SetRequiredRole` command."
            color = infoColor


            field {
                name = "${guild.name} Roles"
                value = guild.roles.joinToString("\n") { it.asMention }
            }
        }

        config.guildConfigurations.add(GuildConfiguration(guild.id, prefix, role.idLong))
        config.save()
    }
}