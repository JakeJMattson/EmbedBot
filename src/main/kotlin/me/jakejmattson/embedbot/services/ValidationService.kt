package me.jakejmattson.embedbot.services

import me.jakejmattson.embedbot.dataclasses.*
import me.jakejmattson.embedbot.locale.messages
import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.discord.Discord
import net.dv8tion.jda.api.entities.MessageEmbed

const val CHAR_LIMIT = MessageEmbed.EMBED_MAX_LENGTH_BOT
const val FIELD_LIMIT = 25
const val TITLE_LIMIT = MessageEmbed.TITLE_MAX_LENGTH
const val DESCRIPTION_LIMIT = MessageEmbed.TEXT_MAX_LENGTH
const val FIELD_NAME_LIMIT = MessageEmbed.TITLE_MAX_LENGTH
const val FIELD_VALUE_LIMIT = MessageEmbed.VALUE_MAX_LENGTH
const val FOOTER_LIMIT = MessageEmbed.TEXT_MAX_LENGTH
const val AUTHOR_LIMIT = MessageEmbed.TITLE_MAX_LENGTH

@Service
class ValidationService(private val configuration: Configuration, discord: Discord) {
    private val jda = discord.jda

    fun validateConfiguration(): OperationResult {
        if (configuration.prefix.isEmpty())
            return false withMessage messages.errors.EMPTY_PREFIX

        if (configuration.botOwner.isEmpty())
            return false withMessage messages.errors.EMPTY_BOT_OWNER

        jda.retrieveUserById(configuration.botOwner).complete()
            ?: return false withMessage messages.errors.INVALID_USER

        return true withMessage "Configuration file validated!"
    }
}