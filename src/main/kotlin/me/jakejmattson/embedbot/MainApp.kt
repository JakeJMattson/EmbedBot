package me.jakejmattson.embedbot

import me.jakejmattson.embedbot.dataclasses.Configuration
import me.jakejmattson.embedbot.extensions.requiredPermissionLevel
import me.jakejmattson.embedbot.locale.messages
import me.jakejmattson.embedbot.services.*
import me.jakejmattson.kutils.api.dsl.bot
import me.jakejmattson.kutils.api.extensions.jda.*
import java.awt.Color

lateinit var discordToken: String

fun main(args: Array<String>) {
    discordToken = args.firstOrNull() ?: throw IllegalArgumentException(messages.errors.NO_ARGS)

    bot(discordToken) {
        configure {
            val (configuration, validationService, permissionsService)
                = it.getInjectionObjects(Configuration::class, ValidationService::class, PermissionsService::class)

            with(validationService.validateConfiguration()) {
                require(wasSuccessful) { message }
                println(message)
            }

            commandReaction = null

            prefix {
                configuration.prefix
            }

            colors {
                infoColor = Color(0x00BFFF)
            }

            mentionEmbed {
                val discord = it.discord
                val properties = discord.properties
                val self = discord.jda.selfUser
                val requiredRole = configuration.getGuildConfig(it.guild?.id ?: "")?.requiredRole ?: "<Not Configured>"

                author {
                    discord.jda.retrieveUserById(254786431656919051).queue {
                        iconUrl = it.effectiveAvatarUrl
                        name = it.fullName()
                        url = messages.links.DISCORD_ACCOUNT
                    }
                }

                simpleTitle = "${self.fullName()} (EmbedBot 2.0.1)"
                description = messages.project.BOT
                thumbnail = self.effectiveAvatarUrl
                color = infoColor

                addInlineField("Required role", requiredRole)
                addInlineField("Prefix", configuration.prefix)
                addInlineField("Build Info", "`${properties.kutilsVersion} - ${properties.jdaVersion}`")
                addInlineField("Source", messages.project.REPO)
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