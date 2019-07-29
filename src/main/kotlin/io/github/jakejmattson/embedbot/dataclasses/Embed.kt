package io.github.jakejmattson.embedbot.dataclasses

import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.services.Field
import io.github.jakejmattson.embedbot.utilities.gson
import me.aberrantfox.kjdautils.internal.command.tryRetrieveSnowflake
import net.dv8tion.jda.core.*
import net.dv8tion.jda.core.entities.*
import java.time.temporal.TemporalAccessor
import kotlin.streams.toList

data class Embed(var name: String,
                 private val builder: EmbedBuilder = EmbedBuilder(),
                 private val copyLocation: CopyLocation? = null) {

    val isEmpty: Boolean
        get() = builder.isEmpty

    val fields: List<Field>
        get() = builder.fields

    val fieldCount: Int
        get() = fields.size

    val charCount: Int
        get() = if (!isEmpty) build().length else 0

    val copyLocationString: String
        get() = copyLocation?.toString() ?: "<Not copied>"

    fun setAuthor(name: String, iconUrl: String) = builder.setAuthor(name, null, iconUrl)!!
    fun setColor(color: Int) = builder.setColor(color)!!
    fun setDescription(description: String) = builder.setDescription(description)!!
    fun setFooter(text: String, iconUrl: String) = builder.setFooter(text, iconUrl)!!
    fun setImage(url: String) = builder.setImage(url)!!
    fun setThumbnail(url: String) = builder.setThumbnail(url)!!
    fun setTimestamp(time: TemporalAccessor) = builder.setTimestamp(time)!!
    fun setTitle(title: String) = builder.setTitle(title)!!

    fun clearAuthor() = builder.setAuthor(null)!!
    fun clearColor() = builder.setColor(null)!!
    fun clearDescription() = builder.setDescription(null)!!
    fun clearFooter() = builder.setFooter(null, null)!!
    fun clearImage() = builder.setImage(null)!!
    fun clearThumbnail() = builder.setThumbnail(null)!!
    fun clearTimestamp() = builder.setTimestamp(null)!!
    fun clearTitle() = builder.setTitle(null)!!

    private fun setFields(fields: List<Field>) = clearFields().also { fields.forEach { builder.addField(it) } }
    fun setField(index: Int, field: Field) { builder.fields[index] = field }
    fun addField(field: Field) = builder.addField(field)!!
    fun removeField(index: Int) = builder.fields.removeAt(index)!!

    fun clear() = builder.clear()!!
    fun clearFields() = builder.clearFields()!!
    fun clearNonFields() {
        val fields = fields.stream().toList()
        clear()
        setFields(fields)
    }

    fun build() = builder.build()!!
    fun toJson() = gson.toJson(builder)!!

    fun isLoaded(guild: Guild) = guild.getLoadedEmbed() == this

    fun update(jda: JDA): UpdateResponse {
        val original = copyLocation
            ?: return UpdateResponse(false, "This embed was not copied from another message.")

        val channel = jda.getTextChannelById(original.channelId)
            ?: return UpdateResponse(false, "The channel this embed was copied from no longer exists.")

        val message = tryRetrieveSnowflake(jda) {
            channel.getMessageById(original.messageId).complete()
        } as Message? ?: return UpdateResponse(false,  "The message this embed was copied from no longer exists.")

        if (message.author != jda.selfUser)
            return UpdateResponse(false, "The message this embed was copied from is not from this bot.")

        if (isEmpty)
            return UpdateResponse(false, "Cannot build an empty embed.")

        val currentEmbed = message.getEmbed()
            ?: return UpdateResponse(false, "Target message has no embed.")

        if (currentEmbed == builder.build())
            return UpdateResponse(false, "This message is up to date.")

        message.editMessage(build()).complete()

        return UpdateResponse(true)
    }
}

data class UpdateResponse(val canUpdate: Boolean, val reason: String = "")
data class CopyLocation(val channelId: String, val messageId: String) {
    override fun toString() = "Channel ID: $channelId\nMessage ID: $messageId"
}