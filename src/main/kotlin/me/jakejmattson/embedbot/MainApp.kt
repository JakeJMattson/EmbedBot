package me.jakejmattson.embedbot

import me.jakejmattson.discordkt.api.dsl.bot
import me.jakejmattson.discordkt.api.extensions.jda.*
import me.jakejmattson.embedbot.dataclasses.Configuration
import me.jakejmattson.embedbot.extensions.requiredPermissionLevel
import me.jakejmattson.embedbot.services.PermissionsService
import java.awt.Color

fun main(args: Array<String>) {
    val token = args.firstOrNull()
        ?: throw IllegalArgumentException("No program arguments provided. Expected bot token.")

    bot(token) {
        configure {
            val (configuration, permissionsService)
                = it.getInjectionObjects(Configuration::class, PermissionsService::class)

            commandReaction = null

            prefix {
                it.guild?.let { configuration[it.idLong]?.prefix } ?: "<none>"
            }

            colors {
                infoColor = Color(0x00BFFF)
            }

            mentionEmbed {
                val discord = it.discord
                val jda = discord.jda
                val properties = discord.properties
                val role = it.guild?.idLong?.let { configuration[it]?.getLiveRole(jda)?.asMention } ?: "<None>"

                author {
                    jda.retrieveUserById(254786431656919051).queue {
                        iconUrl = it.effectiveAvatarUrl
                        name = it.fullName()
                        url = "https://discordapp.com/users/254786431656919051/"
                    }
                }

                title {
                    text = "EmbedBot"
                    url = "https://discordapp.com/oauth2/authorize?client_id=439163847618592782&scope=bot&permissions=101440"
                }

                description = "A bot for creating and managing embeds."
                thumbnail = jda.selfUser.effectiveAvatarUrl
                color = infoColor

                addInlineField("Prefix", it.relevantPrefix)
                addInlineField("Role", role)

                addField("Build", "`2.1.0 - ${properties.libraryVersion} - ${properties.jdaVersion}`")
                addInlineField("Source Code", "https://github.com/JakeJMattson/EmbedBot")
            }

            visibilityPredicate {
                val guild = it.guild ?: return@visibilityPredicate false
                val member = it.user.toMember(guild)!!
                val permission = it.command.requiredPermissionLevel

                permissionsService.hasClearance(member, permission)
            }

            it.jda.guilds.forEach {
                configuration.setup(it)
            }
        }
    }
}