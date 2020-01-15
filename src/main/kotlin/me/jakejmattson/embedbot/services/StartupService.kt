package me.jakejmattson.embedbot.services

import com.google.gson.Gson
import me.jakejmattson.embedbot.dataclasses.Configuration
import me.jakejmattson.embedbot.extensions.requiredPermissionLevel
import me.jakejmattson.embedbot.locale.messages
import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.api.dsl.command.Command
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.discord.Discord
import me.aberrantfox.kjdautils.extensions.jda.*
import net.dv8tion.jda.api.entities.*
import java.awt.Color

data class Properties(val author: String, val version: String, val kutils: String, val repository: String)

private val propFile = Properties::class.java.getResource("/properties.json").readText()
val project = Gson().fromJson(propFile, Properties::class.java)

@Service
class StartupService(configuration: Configuration,
                     discord: Discord,
                     validationService: ValidationService,
                     permissionsService: PermissionsService) {
    init {
        with(validationService.validateConfiguration()) {
            require(wasSuccessful) { message }
            println(message)
        }

        with(discord.configuration) {
            prefix = configuration.prefix
            reactToCommands = false

            mentionEmbed = {
                embed {
                    val self = it.guild.jda.selfUser
                    val requiredRole = configuration.getGuildConfig(it.guild.id)?.requiredRole ?: "<Not Configured>"

                    color = Color(0x00bfff)
                    thumbnail = self.effectiveAvatarUrl
                    addField(self.fullName(), messages.descriptions.BOT)
                    addInlineField("Required role", requiredRole)
                    addInlineField("Prefix", configuration.prefix)

                    with(project) {
                        val kotlinVersion = KotlinVersion.CURRENT

                        addField("Build Info", "```" +
                            "Version: $version\n" +
                            "KUtils: $kutils\n" +
                            "Kotlin: $kotlinVersion" +
                            "```")

                        addInlineField("Author", "[${author}](${messages.links.DISCORD_ACCOUNT})")
                        addInlineField("Source", repository)
                    }
                }
            }

            visibilityPredicate = predicate@{ command: Command, user: User, _: MessageChannel, guild: Guild? ->
                guild ?: return@predicate false

                val member = user.toMember(guild)!!
                val permission = command.requiredPermissionLevel

                permissionsService.hasClearance(member, permission)
            }
        }
    }
}