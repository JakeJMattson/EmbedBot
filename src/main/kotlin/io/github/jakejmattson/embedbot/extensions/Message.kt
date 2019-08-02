package io.github.jakejmattson.embedbot.extensions

import me.aberrantfox.kjdautils.extensions.jda.containsURL
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.*

fun Message.getEmbed() =
    if (containsURL() || embeds.isEmpty())
        null
    else
        embeds.first()

fun MessageEmbed.toEmbedBuilder() =
        EmbedBuilder().apply {
            setTitle(title)
            setDescription(description)
            setFooter(footer?.text, footer?.iconUrl)
            setThumbnail(thumbnail?.url)
            setTimestamp(timestamp)
            setImage(image?.url)
            setColor(colorRaw)
            setAuthor(author?.name)
            fields.addAll(this@toEmbedBuilder.fields)
        }