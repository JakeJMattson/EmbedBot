package io.github.jakejmattson.embedbot.services

import com.google.gson.GsonBuilder
import me.aberrantfox.kjdautils.api.annotation.Service
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.*
import java.time.temporal.*

typealias Field = MessageEmbed.Field

private val embedMap = HashMap<String, GuildEmbeds>()
private val gson = GsonBuilder().setPrettyPrinting().create()

data class Embed(val name: String, private val builder: EmbedBuilder = EmbedBuilder()) {
    val isEmpty: Boolean
        get() = builder.isEmpty

    val fields: List<Field>
        get() = builder.fields

    val lastFieldIndex: Int
        get() = builder.fields.lastIndex

    fun setAuthor(author: String) = builder.setAuthor(author)
    fun setColor(color: Int) = builder.setColor(color)
    fun setDescription(description: String) = builder.setDescription(description)
    fun setFooter(text: String, iconUrl: String) = builder.setFooter(text, iconUrl)
    fun setImage(url: String) = builder.setImage(url)
    fun setThumbnail(url: String) = builder.setThumbnail(url)
    fun setTimestamp(time: TemporalAccessor) = builder.setTimestamp(time)
    fun setTitle(title: String) = builder.setTitle(title)

    fun clearAuthor() = builder.setAuthor(null)
    fun clearColor() = builder.setColor(null)
    fun clearDescription() = builder.setDescription(null)
    fun clearFooter() = builder.setFooter(null, null)
    fun clearImage() = builder.setImage(null)
    fun clearThumbnail() = builder.setThumbnail(null)
    fun clearTimestamp() = builder.setTimestamp(null)
    fun clearTitle() = builder.setTitle(null)

    fun setFields(fields: List<Field>) = clearFields().also { fields.forEach { builder.addField(it) } }
    fun setField(index: Int, field: Field) { builder.fields[index] = field }
    fun addField(field: Field) = builder.addField(field)
    fun removeField(index: Int) = builder.fields.removeAt(index)

    fun clear() = builder.clear()
    fun clearFields() = builder.clearFields()

    fun build() = builder.build()
    fun toJson() = gson.toJson(builder)

    fun isLoaded(guild: Guild) = guild.getGuildEmbeds().loadedEmbed == this
}

data class GuildEmbeds(var loadedEmbed: Embed?, val embedList: ArrayList<Embed>) {
    fun addAndLoad(embed: Embed) {
        embedList.add(embed)
        load(embed)
    }

    fun load(embed: Embed) {
        loadedEmbed = embed
    }
}

fun createEmbedFromJson(name: String, json: String) = Embed(name, gson.fromJson(json, EmbedBuilder::class.java))

fun Guild.getGuildEmbeds() = embedMap.getOrPut(this.id) { GuildEmbeds(null, arrayListOf()) }
fun Guild.getLoadedEmbed() = getGuildEmbeds().loadedEmbed
fun Guild.hasEmbedWithName(name: String) = getGuildEmbeds().embedList.any { it.name.toLowerCase() == name.toLowerCase() }
fun Guild.loadEmbed(embed: Embed) = getGuildEmbeds().load(embed)
fun Guild.listEmbeds() = getGuildEmbeds().embedList.sortedBy { it.name }.joinToString("\n") { it.name }

@Service
class EmbedService {
    fun createEmbed(guild: Guild, name: String): Boolean {
        val embeds = guild.getGuildEmbeds()

        if (guild.hasEmbedWithName(name))
            return false

        val newEmbed = Embed(name)
        embeds.addAndLoad(newEmbed)
        return true
    }

    fun addEmbed(guild: Guild, embed: Embed): Boolean {
        val embeds = guild.getGuildEmbeds()

        if (embed in embeds.embedList)
            return false

        embeds.addAndLoad(embed)
        return true
    }

    fun removeEmbed(guild: Guild, embed: Embed): Boolean {
        val embeds = guild.getGuildEmbeds()

        if (embed !in embeds.embedList)
            return false

        if (embed.isLoaded(guild))
            embeds.loadedEmbed = null

        embeds.embedList.remove(embed)
        return true
    }
}

fun MessageEmbed.toEmbed(name: String) =
    Embed(name, EmbedBuilder()
        .setTitle(title)
        .setDescription(description)
        .setFooter(footer?.text, footer?.iconUrl)
        .setThumbnail(thumbnail?.url)
        .setTimestamp(timestamp)
        .setImage(image?.url)
        .setColor(colorRaw)
        .setAuthor(author?.name)
    ).also { it.setFields(fields) }