package io.github.jakejmattson.embedbot.services

import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.api.dsl.embed
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.*

data class Embed(val name: String, val builder: EmbedBuilder = EmbedBuilder()) {
    fun setTitle(title: String) = builder.setTitle(title)
    fun setDescription(description: String) = builder.setDescription(description)
    fun setFooter(text: String, iconUrl: String) = builder.setFooter(text, iconUrl)
    fun setThumbnail(url: String) = builder.setThumbnail(url)
    fun setImage(url: String) = builder.setImage(url)
    fun setColor(color: Int) = builder.setColor(color)
    fun setAuthor(author: String) = builder.setAuthor(author)
}

private val embedMap = HashMap<String, ArrayList<Embed>>()

fun getGuildEmbeds(guildId: String) : ArrayList<Embed> {
    if (!embedMap.containsKey(guildId))
        embedMap[guildId] = arrayListOf()

    return embedMap[guildId]!!
}

@Service
class EmbedService {
    fun addEmbed(guild: Guild, name: String) {
        getGuildEmbeds(guild.id).add(Embed(name))
    }

    fun removeEmbed(guild: Guild, embed: Embed) {
        getGuildEmbeds(guild.id).remove(embed)
    }

    fun listEmbeds(guild: Guild) = getGuildEmbeds(guild.id).joinToString("\n") { it.name }
}