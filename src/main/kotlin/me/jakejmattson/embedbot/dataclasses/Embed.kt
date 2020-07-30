package me.jakejmattson.embedbot.dataclasses

import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.locale.messages
import me.jakejmattson.embedbot.services.*
import me.jakejmattson.kutils.api.extensions.jda.tryRetrieveSnowflake
import net.dv8tion.jda.api.*
import net.dv8tion.jda.api.entities.*
import java.awt.Color
import java.time.temporal.TemporalAccessor
import kotlin.streams.toList

private fun EmbedBuilder.save(): EmbedBuilder {
    saveEmbeds()
    return this
}

data class CopyLocation(val channelId: String, val messageId: String) {
    override fun toString() = "Channel ID: $channelId\nMessage ID: $messageId"
}

data class Embed(var name: String,
                 private val builder: EmbedBuilder = EmbedBuilder(),
                 var copyLocation: CopyLocation? = null) {

    private val fields: MutableList<Field>
        get() = builder.fields

    val isEmpty: Boolean
        get() = builder.isEmpty

    val fieldCount: Int
        get() = fields.size

    val charCount: Int
        get() = if (!isEmpty) build().length else 0

    val copyLocationString: String
        get() = copyLocation?.toString() ?: "<Not copied>"

    fun setAuthor(name: String, iconUrl: String) = builder.setAuthor(name, null, iconUrl).save()
    fun setColor(color: Color) = builder.setColor(color).save()
    fun setDescription(description: String) = builder.setDescription(description).save()
    fun setFooter(text: String, iconUrl: String) = builder.setFooter(text, iconUrl).save()
    fun setImage(url: String) = builder.setImage(url).save()
    fun setThumbnail(url: String) = builder.setThumbnail(url).save()
    fun setTimestamp(time: TemporalAccessor) = builder.setTimestamp(time).save()
    fun setTitle(title: String) = builder.setTitle(title).save()

    fun clearAuthor() = builder.setAuthor(null).save()
    fun clearColor() = builder.setColor(null).save()
    fun clearDescription() = builder.setDescription(null).save()
    fun clearFooter() = builder.setFooter(null, null).save()
    fun clearImage() = builder.setImage(null).save()
    fun clearThumbnail() = builder.setThumbnail(null).save()
    fun clearTimestamp() = builder.setTimestamp(null).save()
    fun clearTitle() = builder.setTitle(null).save()

    private fun setFields(fields: List<Field>) = clearFields().also { fields.forEach { builder.addField(it) } }
    fun setField(index: Int, field: Field) {
        fields[index] = field
    }

    fun addField(field: Field) = builder.addField(field)
    fun addBlankField(isInline: Boolean) = builder.addBlankField(isInline)
    fun removeField(index: Int) = fields.removeAt(index)
    fun insertField(index: Int, field: Field) = fields.add(index, field)
    fun setFieldName(index: Int, name: String) = with(fields[index]) { setField(index, Field(name, value, isInline)) }
    fun setFieldText(index: Int, value: String) = with(fields[index]) { setField(index, Field(name, value, isInline)) }
    fun setFieldInline(index: Int, isInline: Boolean) = with(fields[index]) { setField(index, Field(name, value, isInline)) }

    fun clear() = builder.clear().save()
    fun clearFields() = builder.clearFields().save()
    fun clearNonFields() {
        val fields = fields.stream().toList()
        clear()
        setFields(fields)
    }

    fun build() = builder.build()
    fun toJson() = gson.toJson(builder)!!

    fun isLoaded(guild: Guild) = guild.getLoadedEmbed() == this

    fun update(jda: JDA, channelId: String, messageId: String): OperationResult {
        val channel = jda.getTextChannelById(channelId)
            ?: return false withMessage "Target channel does not exist."

        val message = jda.tryRetrieveSnowflake {
            channel.retrieveMessageById(messageId).complete()
        } as Message? ?: return false withMessage "Target message does not exist."

        if (message.author != jda.selfUser)
            return false withMessage "Target message is not from this bot."

        if (isEmpty)
            return false withMessage messages.EMPTY_EMBED

        val currentEmbed = message.getEmbed()
            ?: return false withMessage "Target message has no embed."

        if (currentEmbed == builder.build())
            return false withMessage "This embed is up to date."

        message.editMessage(build()).queue()

        return true withMessage "Update successful."
    }
}