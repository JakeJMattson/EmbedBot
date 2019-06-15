package io.github.jakejmattson.embedbot.preconditions

import io.github.jakejmattson.embedbot.dataclasses.Configuration
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.aberrantfox.kjdautils.internal.command.*

@Precondition
fun produceIsGuildOwnerPrecondition(configuration: Configuration) = exit@{ event: CommandEvent ->
    val category = event.container.commands[event.commandStruct.commandName]?.category ?: return@exit Pass

    val guild = event.guild
        ?: return@exit Fail("This can only be executed within a guild.")

    val member = event.author.toMember(guild)

    if (!member.isOwner && category == "GuildConfiguration")
        return@exit Fail("Missing clearance to use this command. You must be the guild owner.")

    return@exit Pass
}