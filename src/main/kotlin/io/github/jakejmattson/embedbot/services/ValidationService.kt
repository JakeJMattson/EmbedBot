package io.github.jakejmattson.embedbot.services

import io.github.jakejmattson.embedbot.dataclasses.*
import io.github.jakejmattson.embedbot.locale.messages
import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.discord.Discord

@Service
class ValidationService(private val configuration: Configuration, discord: Discord) {
    private val jda = discord.jda

    init {
        with(validateConfiguration()) {
            require(wasSuccessful) { message }
            println(message)
        }
    }

    private fun validateConfiguration(): OperationResult {
        if (configuration.prefix.isEmpty())
            return false withMessage messages.errors.EMPTY_PREFIX

        if (configuration.botOwner.isEmpty())
            return false withMessage messages.errors.EMPTY_BOT_OWNER

        jda.retrieveUserById(configuration.botOwner).complete()
            ?: return false withMessage messages.errors.INVALID_USER

        return true withMessage "Configuration file validated!"
    }
}