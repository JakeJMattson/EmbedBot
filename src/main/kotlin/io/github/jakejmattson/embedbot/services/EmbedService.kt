package io.github.jakejmattson.embedbot.services

import me.aberrantfox.kjdautils.api.annotation.Service
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

data class GuildEmbeds(var loadedEmbed: Embed?, val embedList: ArrayList<Embed>)

private val embedMap = HashMap<String, GuildEmbeds>()

fun Embed.isLoaded(guild: Guild) = getGuildEmbeds(guild.id).loadedEmbed == this

fun getGuildEmbeds(guildId: String) : GuildEmbeds {
    if (!embedMap.containsKey(guildId))
        embedMap[guildId] = GuildEmbeds(null, arrayListOf())

    return embedMap[guildId]!!
}

@Service
class EmbedService {
    fun addEmbed(guild: Guild, name: String) {
        val embeds = getGuildEmbeds(guild.id)

        if (embeds.embedList.any { it.name == name })
            return

        val newEmbed = Embed(name)
        embeds.loadedEmbed = newEmbed
        embeds.embedList.add(newEmbed)
    }

    fun removeEmbed(guild: Guild, embed: Embed) {
        val embeds = getGuildEmbeds(guild.id)

        if (embed.isLoaded(guild))
            embeds.loadedEmbed = null

        getGuildEmbeds(guild.id).embedList.remove(embed)
    }

    fun loadEmbed(guild: Guild, embed: Embed) {
        getGuildEmbeds(guild.id).loadedEmbed = embed
    }

    fun getLoadedEmbed(guild: Guild) = getGuildEmbeds(guild.id).loadedEmbed

    fun listEmbeds(guild: Guild) = getGuildEmbeds(guild.id).embedList.joinToString("\n") { it.name }
}