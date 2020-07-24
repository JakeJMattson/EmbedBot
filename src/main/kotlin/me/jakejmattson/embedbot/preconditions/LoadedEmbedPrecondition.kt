package me.jakejmattson.embedbot.preconditions

import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.locale.messages
import me.jakejmattson.kutils.api.dsl.command.CommandEvent
import me.jakejmattson.kutils.api.dsl.preconditions.*

class LoadedEmbedPrecondition : Precondition() {
    override fun evaluate(event: CommandEvent<*>): PreconditionResult {
        val command = event.command ?: return Pass
        val guild = event.guild ?: return Fail(messages.errors.MISSING_GUILD)

        if (command.requiresLoadedEmbed && !guild.hasLoadedEmbed())
            return Fail(messages.errors.MISSING_EMBED)

        return Pass
    }

}