package io.github.jakejmattson.embedbot.preconditions

import io.github.jakejmattson.embedbot.extensions.requiredPermissionLevel
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.aberrantfox.kjdautils.internal.command.*

@Precondition
fun produceHasPermissionPrecondition(permissionsService: PermissionsService) = exit@{ event: CommandEvent ->
    val command = event.container.commands[event.commandStruct.commandName]
    val requiredPermissionLevel = command?.requiredPermissionLevel ?: Permission.NONE
    val guild = event.guild ?: return@exit Fail("This can only be executed within a guild.")
    val member = event.author.toMember(guild)!!

    if (!permissionsService.hasClearance(member, requiredPermissionLevel))
        return@exit Fail("Missing clearance to use this command.")

    return@exit Pass
}