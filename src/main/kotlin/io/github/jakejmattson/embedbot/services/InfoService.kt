package io.github.jakejmattson.embedbot.services

import com.google.gson.Gson
import io.github.jakejmattson.embedbot.dataclasses.Configuration
import io.github.jakejmattson.embedbot.locale.messages
import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.extensions.jda.fullName
import net.dv8tion.jda.api.entities.Guild
import java.awt.Color

const val CHAR_LIMIT = 6000
const val FIELD_LIMIT = 25
const val TITLE_LIMIT = 256
const val DESCRIPTION_LIMIT = 2048
const val FIELD_NAME_LIMIT = 256
const val FIELD_VALUE_LIMIT = 1024
const val FOOTER_LIMIT = 2048
const val AUTHOR_LIMIT = 256

@Service
class InfoService(private val configuration: Configuration) {
    private data class Properties(val version: String, val author: String, val repository: String)
    private val propFile = Properties::class.java.getResource("/properties.json").readText()
    private val project = Gson().fromJson(propFile, Properties::class.java)

    fun botInfo(guild: Guild) = embed {
        val self = guild.jda.selfUser
        val requiredRole = configuration.getGuildConfig(guild.id)?.requiredRole ?: "<Not Configured>"

        color = Color(0x00bfff)
        thumbnail = self.effectiveAvatarUrl
        addField(self.fullName(), messages.descriptions.BOT)
        addInlineField("Required role", requiredRole)
        addInlineField("Prefix", configuration.prefix)
        addInlineField("Author", "[${project.author}](${messages.links.DISCORD_ACCOUNT})")
        addInlineField("Version", project.version)
        addInlineField("Source", project.repository)
    }
}