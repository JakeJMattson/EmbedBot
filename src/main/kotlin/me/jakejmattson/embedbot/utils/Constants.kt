package me.jakejmattson.embedbot.utils

import net.dv8tion.jda.api.entities.MessageEmbed

const val CHAR_LIMIT = MessageEmbed.EMBED_MAX_LENGTH_BOT
const val FIELD_LIMIT = 25
const val TITLE_LIMIT = MessageEmbed.TITLE_MAX_LENGTH
const val DESCRIPTION_LIMIT = MessageEmbed.TEXT_MAX_LENGTH
const val FIELD_NAME_LIMIT = MessageEmbed.TITLE_MAX_LENGTH
const val FIELD_VALUE_LIMIT = MessageEmbed.VALUE_MAX_LENGTH
const val FOOTER_LIMIT = MessageEmbed.TEXT_MAX_LENGTH
const val AUTHOR_LIMIT = MessageEmbed.TITLE_MAX_LENGTH

val messages = Messages()

class Messages(
    val MISSING_GUILD: String = "Must be invoked inside a guild.",
    val EMBED_ALREADY_EXISTS: String = "An embed with this name already exists.",
    val GROUP_ALREADY_EXISTS: String = "A group with this name already exists.",
    val NO_LOCATION: String = "This embed has no location set.",
    val MISSING_OPTIONAL_EMBED: String = "Please load an embed or specify one explicitly.",
    val MISSING_EMBED: String = "No embed loaded!",
    val EMPTY_EMBED: String = "Cannot build an empty embed."
)