package io.github.jakejmattson.embedbot.services

import com.google.gson.Gson
import io.github.jakejmattson.embedbot.dataclasses.Configuration
import io.github.jakejmattson.embedbot.extensions.addField
import io.github.jakejmattson.embedbot.extensions.addInlineField
import io.github.jakejmattson.embedbot.extensions.color
import io.github.jakejmattson.embedbot.extensions.thumbnail
import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.extensions.jda.fullName
import net.dv8tion.jda.core.entities.Guild
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

    val version = project.version
    val author = project.author
    val source = project.repository

    fun botInfo(guild: Guild) = embed {
        val self = guild.jda.selfUser
        val requiredRole = configuration.getGuildConfig(guild.id)?.requiredRole ?: "<Not Configured>"

        color = Color.green
        thumbnail = self.effectiveAvatarUrl
        addField(self.fullName(), "A bot for creating and managing embeds.")
        addInlineField("Author", "[$author](https://discordapp.com/users/254786431656919051/)")
        addInlineField("Version", version)
        addInlineField("Prefix", configuration.prefix)
        addInlineField("Required role", requiredRole)
        addInlineField("Source", source)
    }
}