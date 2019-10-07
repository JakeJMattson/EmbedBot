package io.github.jakejmattson.embedbot.services

import com.google.gson.Gson
import io.github.jakejmattson.embedbot.dataclasses.Configuration
import io.github.jakejmattson.embedbot.locale.messages
import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.extensions.jda.fullName
import net.dv8tion.jda.api.entities.*
import java.awt.Color

const val CHAR_LIMIT = MessageEmbed.EMBED_MAX_LENGTH_BOT
const val FIELD_LIMIT = 25
const val TITLE_LIMIT = MessageEmbed.TITLE_MAX_LENGTH
const val DESCRIPTION_LIMIT = MessageEmbed.TEXT_MAX_LENGTH
const val FIELD_NAME_LIMIT = MessageEmbed.TITLE_MAX_LENGTH
const val FIELD_VALUE_LIMIT = MessageEmbed.VALUE_MAX_LENGTH
const val FOOTER_LIMIT = MessageEmbed.TEXT_MAX_LENGTH
const val AUTHOR_LIMIT = MessageEmbed.TITLE_MAX_LENGTH

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