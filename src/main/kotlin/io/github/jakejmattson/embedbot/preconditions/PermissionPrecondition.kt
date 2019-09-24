package io.github.jakejmattson.embedbot.preconditions

import io.github.jakejmattson.embedbot.extensions.requiredPermissionLevel
import io.github.jakejmattson.embedbot.locale.messages
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.aberrantfox.kjdautils.internal.command.*

@Precondition
fun produceHasPermissionPrecondition(permissionsService: PermissionsService) = precondition { event: CommandEvent ->
    val command = event.container.commands[event.commandStruct.commandName]
    val requiredPermissionLevel = command?.requiredPermissionLevel ?: DEFAULT_REQUIRED_PERMISSION
    val guild = event.guild ?: return@precondition Fail(messages.errors.MISSING_GUILD)
    val member = event.author.toMember(guild)!!

    if (!permissionsService.hasClearance(member, requiredPermissionLevel))
        return@precondition Fail(messages.errors.MISSING_CLEARANCE)

    return@precondition Pass
}