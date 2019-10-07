package io.github.jakejmattson.embedbot.preconditions

import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.locale.messages
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.*

@Precondition
fun produceHasLoadedEmbedPrecondition() = precondition { event: CommandEvent<*> ->
    val command = event.container.commands[event.commandStruct.commandName] ?: return@precondition Pass

    val guild = event.guild ?: return@precondition Fail(messages.errors.MISSING_GUILD)

    if (command.requiresLoadedEmbed && !guild.hasLoadedEmbed())
        return@precondition Fail(messages.errors.MISSING_EMBED)

    return@precondition Pass
}