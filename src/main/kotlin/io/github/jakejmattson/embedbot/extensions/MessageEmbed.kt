package io.github.jakejmattson.embedbot.extensions

import io.github.jakejmattson.embedbot.dataclasses.Embed
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageEmbed

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