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
            allowMentionPrefix = true

            prefix {
                it.guild?.let { configuration[it.idLong]?.prefix } ?: "<none>"
            }

            colors {
                infoColor = Color(0x00BFFF)
            }

            mentionEmbed {
                val guild = it.guild ?: return@mentionEmbed
                val jda = it.discord.jda
                val properties = it.discord.properties
                val prefix = it.relevantPrefix
                val role = configuration[guild.idLong]?.getLiveRole(jda)?.takeUnless { it == guild.publicRole }?.asMention
                    ?: ""

                author {
                    jda.retrieveUserById(254786431656919051).queue { user ->
                        iconUrl = user.effectiveAvatarUrl
                        name = user.fullName()
                        url = user.profileLink
                    }
                }

                simpleTitle = "EmbedBot"
                thumbnail = jda.selfUser.effectiveAvatarUrl
                color = infoColor
                description = (if (role.isNotBlank()) "You must have $role" else "") +
                    "\nCurrent prefix is `$prefix`" +
                    "\nUse `${prefix}help` to see commands."

                addInlineField("", "[[Invite Me]](https://discordapp.com/oauth2/authorize?client_id=439163847618592782&scope=bot&permissions=101440)")
                addInlineField("", "[[See Code]](https://github.com/JakeJMattson/EmbedBot)")
                addInlineField("", "[[User Guide]](https://github.com/JakeJMattson/EmbedBot/blob/master/guide.md)")

                footer {
                    text = "2.1.0 - ${properties.libraryVersion} - ${properties.jdaVersion}"
                }
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