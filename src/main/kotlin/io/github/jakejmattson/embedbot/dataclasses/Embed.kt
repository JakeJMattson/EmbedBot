package io.github.jakejmattson.embedbot.dataclasses

import io.github.jakejmattson.embedbot.services.*
import io.github.jakejmattson.embedbot.utilities.gson
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Guild
import java.time.temporal.TemporalAccessor

data class Embed(val name: String, private val builder: EmbedBuilder = EmbedBuilder()) {
    val isEmpty: Boolean
        get() = builder.isEmpty

    val fields: List<Field>
        get() = builder.fields

    val lastFieldIndex: Int
        get() = fields.lastIndex

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