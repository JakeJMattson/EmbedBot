package me.jakejmattson.embedbot.preconditions

import me.jakejmattson.discordkt.api.dsl.command.CommandEvent
import me.jakejmattson.discordkt.api.dsl.preconditions.*
import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.utils.messages

class LoadedEmbedPrecondition : Precondition() {
    override fun evaluate(event: CommandEvent<*>): PreconditionResult {
        val command = event.command ?: return Pass
        val guild = event.guild ?: return Fail(messages.MISSING_GUILD)

        if (command.requiresLoadedEmbed && !guild.hasLoadedEmbed())
            return Fail(messages.MISSING_EMBED)

        return Pass
    }

}