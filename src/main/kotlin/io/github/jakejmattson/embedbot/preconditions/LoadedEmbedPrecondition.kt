package io.github.jakejmattson.embedbot.preconditions

import io.github.jakejmattson.embedbot.extensions.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.*

@Precondition
fun produceHasLoadedEmbedPrecondition() = exit@{ event: CommandEvent ->
    val command = event.container.commands[event.commandStruct.commandName] ?: return@exit Pass

    val guild = event.guild
        ?: return@exit Fail("This can only be executed within a guild.")

    if (command.requiresLoadedEmbed && !guild.hasLoadedEmbed())
        return@exit Fail("No embed loaded!")

    return@exit Pass
}