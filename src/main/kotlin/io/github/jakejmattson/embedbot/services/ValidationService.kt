package io.github.jakejmattson.embedbot.services

import io.github.jakejmattson.embedbot.dataclasses.*
import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.discord.Discord

@Service
class ValidationService(private val configuration: Configuration, private val discord: Discord) {
    private val jda = discord.jda

    init {
        with(validateConfiguration()) {
            require(wasSuccessful) { message }
            println(message)
        }
    }

    private fun validateConfiguration(): OperationResult {
        if (configuration.prefix.isEmpty())
            return false withMessage "The configured prefix is empty."

        if (configuration.botOwner.isEmpty())
            return false withMessage "The botOwner field is empty."

        jda.retrieveUserById(configuration.botOwner).complete()
            ?: return false withMessage "Cannot resolve the botOwner ID to a user."

        return true withMessage "Configuration file validated!"
    }
}