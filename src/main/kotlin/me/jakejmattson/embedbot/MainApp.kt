package me.jakejmattson.embedbot

import com.google.gson.Gson
import me.aberrantfox.kjdautils.api.*
import me.aberrantfox.kjdautils.extensions.jda.*
import me.jakejmattson.embedbot.dataclasses.Configuration
import me.jakejmattson.embedbot.extensions.requiredPermissionLevel
import me.jakejmattson.embedbot.locale.messages
import me.jakejmattson.embedbot.services.*
import java.awt.Color

lateinit var discordToken: String

data class Properties(val author: String, val version: String, val repository: String)

private val propFile = Properties::class.java.getResource("/properties.json").readText()
private val project = Gson().fromJson(propFile, Properties::class.java)

fun main(args: Array<String>) {
    discordToken = args.firstOrNull() ?: throw IllegalArgumentException(messages.errors.NO_ARGS)

    startBot(discordToken) {

        registerInjectionObjects(project)

        configure {
            val configuration = discord.getInjectionObject<Configuration>()!!
            val validationService = discord.getInjectionObject<ValidationService>()!!
            val permissionsService = discord.getInjectionObject<PermissionsService>()!!

            with(validationService.validateConfiguration()) {
                require(wasSuccessful) { message }
                println(message)
            }

            prefix = configuration.prefix
            commandReaction = null

            colors {
                infoColor = Color(0x00BFFF)
            }

            mentionEmbed {
                val self = it.guild.jda.selfUser
                val requiredRole = configuration.getGuildConfig(it.guild.id)?.requiredRole ?: "<Not Configured>"

                author {
                    discord.jda.retrieveUserById(254786431656919051).queue {
                        iconUrl = it.effectiveAvatarUrl
                        name = project.author
                        url = messages.links.DISCORD_ACCOUNT
                    }
                }
                
                title = "${self.fullName()} (EmbedBot ${project.version})"
                description = messages.descriptions.BOT
                thumbnail = self.effectiveAvatarUrl
                color = infoColor

                addInlineField("Required role", requiredRole)
                addInlineField("Prefix", configuration.prefix)
                addInlineField("Build Info", "`${discord.properties.version} - ${discord.properties.jdaVersion}`")
                addInlineField("Source", project.repository)
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