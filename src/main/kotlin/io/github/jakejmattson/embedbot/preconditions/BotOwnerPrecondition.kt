package io.github.jakejmattson.embedbot.preconditions

import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.aberrantfox.kjdautils.internal.command.*

@Precondition
fun produceIsBotOwnerPrecondition(permissionsService: PermissionsService) = exit@{ event: CommandEvent ->
    val category = event.container.commands[event.commandStruct.commandName]?.category ?: return@exit Pass

    val guild = event.guild
        ?: return@exit Fail("This can only be executed within a guild.")

    val member = event.author.toMember(guild)

    if (!permissionsService.hasClearance(member, Permission.BOT_OWNER) && category == "BotConfiguration")
        return@exit Fail("Missing clearance to use this command. You must be the bot owner.")

    return@exit Pass
}