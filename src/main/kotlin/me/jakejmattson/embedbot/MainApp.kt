package me.jakejmattson.embedbot

import me.jakejmattson.embedbot.dataclasses.Configuration
import me.jakejmattson.embedbot.extensions.requiredPermissionLevel
import me.jakejmattson.embedbot.services.PermissionsService
import me.jakejmattson.discordkt.api.dsl.bot
import me.jakejmattson.discordkt.api.extensions.jda.*
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
                it.guild?.let { configuration[it.idLong]?.prefix.takeUnless { it.isNullOrBlank() } ?: "=" } ?: "<none>"
            }

            colors {
                infoColor = Color(0x00BFFF)
            }

            mentionEmbed {
                val discord = it.discord
                val jda = discord.jda
                val properties = discord.properties
                val self = jda.selfUser
                val requiredRole = it.guild?.idLong?.let { configuration[it]?.getLiveRole(jda)?.name }
                    ?: "<Not configured>"

                author {
                    jda.retrieveUserById(254786431656919051).queue {
                        iconUrl = it.effectiveAvatarUrl
                        name = it.fullName()
                        url = "https://discordapp.com/users/254786431656919051/"
                    }
                }

                simpleTitle = "${self.fullName()} (EmbedBot 2.0.1)"
                description = "A bot for creating and managing embeds."
                thumbnail = self.effectiveAvatarUrl
                color = infoColor

                addInlineField("Required role", requiredRole)
                addInlineField("Prefix", it.relevantPrefix)
                addInlineField("Build Info", "`${properties.libraryVersion} - ${properties.jdaVersion}`")
                addInlineField("Source", "https://github.com/JakeJMattson/EmbedBot")
            }

            visibilityPredicate {
                val guild = it.guild ?: return@visibilityPredicate false
                val member = it.user.toMember(guild)!!
                val permission = it.command.requiredPermissionLevel

                permissionsService.hasClearance(member, permission)
            }
        }
    }
}