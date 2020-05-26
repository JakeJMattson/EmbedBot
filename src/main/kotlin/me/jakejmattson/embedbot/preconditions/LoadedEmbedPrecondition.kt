package me.jakejmattson.embedbot.preconditions

import me.aberrantfox.kjdautils.api.annotation.Precondition
import me.aberrantfox.kjdautils.internal.command.*
import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.locale.messages

@Precondition
fun produceHasLoadedEmbedPrecondition() = precondition {
    val command = it.command ?: return@precondition Pass

    val guild = it.guild ?: return@precondition Fail(messages.errors.MISSING_GUILD)

    if (command.requiresLoadedEmbed && !guild.hasLoadedEmbed())
        return@precondition Fail(messages.errors.MISSING_EMBED)

    return@precondition Pass
}