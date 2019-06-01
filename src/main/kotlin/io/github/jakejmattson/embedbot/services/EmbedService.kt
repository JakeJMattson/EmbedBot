package io.github.jakejmattson.embedbot.services

import com.google.gson.GsonBuilder
import me.aberrantfox.kjdautils.api.annotation.Service
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.*

typealias Field = MessageEmbed.Field

private val gson = GsonBuilder().setPrettyPrinting().create()

data class Embed(val name: String, private val builder: EmbedBuilder = EmbedBuilder()) {
    val isEmpty: Boolean
        get() = builder.isEmpty

    val fields: List<Field>
        get() = builder.fields

    val lastFieldIndex: Int
        get() = builder.fields.lastIndex

    fun setTitle(title: String) = builder.setTitle(title)
    fun setDescription(description: String) = builder.setDescription(description)
    fun setFooter(text: String, iconUrl: String) = builder.setFooter(text, iconUrl)
    fun setThumbnail(url: String) = builder.setThumbnail(url)
    fun setImage(url: String) = builder.setImage(url)
    fun setColor(color: Int) = builder.setColor(color)
    fun setAuthor(author: String) = builder.setAuthor(author)

    fun setFields(fields: List<Field>) = clearFields().also { fields.forEach { builder.addField(it) } }
    fun setField(index: Int, field: Field) { builder.fields[index] = field }
    fun addField(field: Field) = builder.addField(field)
    fun removeField(index: Int) = builder.fields.removeAt(index)

    fun clearTitle() = builder.setTitle(null)
    fun clearDescription() = builder.setDescription(null)
    fun clearThumbnail() = builder.setThumbnail(null)
    fun clearImage() = builder.setImage(null)
    fun clearColor() = builder.setColor(null)
    fun clearAuthor() = builder.setAuthor(null)

    fun clear() = builder.clear()
    fun clearFields() = builder.clearFields()

    fun build() = builder.build()
    fun toJson() = gson.toJson(builder)
}

fun createEmbedFromJson(name: String, json: String) = Embed(name, gson.fromJson(json, EmbedBuilder::class.java))

data class GuildEmbeds(var loadedEmbed: Embed?, val embedList: ArrayList<Embed>) {
    fun addAndLoad(embed: Embed) {
        loadedEmbed = embed
        embedList.add(embed)
    }
}

private val embedMap = HashMap<String, GuildEmbeds>()

fun Embed.isLoaded(guild: Guild) = getGuildEmbeds(guild.id).loadedEmbed == this

fun getGuildEmbeds(guildId: String): GuildEmbeds {
    if (!embedMap.containsKey(guildId))
        embedMap[guildId] = GuildEmbeds(null, arrayListOf())

    return embedMap[guildId]!!
}

fun Guild.hasEmbedWithName(name: String) = getGuildEmbeds(id).embedList.any { it.name == name }

fun getLoadedEmbed(guild: Guild) = getGuildEmbeds(guild.id).loadedEmbed

@Service
class EmbedService {
    fun createEmbed(guild: Guild, name: String): Boolean {
        val embeds = getGuildEmbeds(guild.id)

        if (guild.hasEmbedWithName(name))
            return false

        val newEmbed = Embed(name)
        embeds.addAndLoad(newEmbed)
        return true
    }

    fun addEmbed(guild: Guild, embed: Embed): Boolean {
        val embeds = getGuildEmbeds(guild.id)

        if (embed in embeds.embedList)
            return false

        embeds.addAndLoad(embed)
        return true
    }

    fun removeEmbed(guild: Guild, embed: Embed): Boolean {
        val embeds = getGuildEmbeds(guild.id)

        if (embed !in embeds.embedList)
            return false

        if (embed.isLoaded(guild))
            embeds.loadedEmbed = null

        embeds.embedList.remove(embed)
        return true
    }

    fun loadEmbed(guild: Guild, embed: Embed) {
        getGuildEmbeds(guild.id).loadedEmbed = embed
    }

    fun listEmbeds(guild: Guild) = getGuildEmbeds(guild.id).embedList.joinToString("\n") { it.name }
}

fun MessageEmbed.toEmbed(name: String) =
    Embed(name, EmbedBuilder()
        .setTitle(title)
        .setDescription(description)
        .setFooter(footer?.text, footer?.iconUrl)
        .setThumbnail(thumbnail?.url)
        .setImage(image?.url)
        .setColor(colorRaw)
        .setAuthor(author?.name)
    ).also { it.setFields(fields) }