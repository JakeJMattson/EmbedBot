package me.jakejmattson.embedbot.preconditions

import me.jakejmattson.embedbot.extensions.requiredPermissionLevel
import me.jakejmattson.embedbot.locale.messages
import me.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.aberrantfox.kjdautils.internal.command.*

@Precondition
fun produceHasPermissionPrecondition(permissionsService: PermissionsService) = precondition {
    val command = it.container[it.commandStruct.commandName] ?: return@precondition Fail()
    val requiredPermissionLevel = command.requiredPermissionLevel
    val guild = it.guild!!
    val member = it.author.toMember(guild)!!

    val response = when (requiredPermissionLevel) {
        Permission.BOT_OWNER -> messages.errors.MISSING_CLEARANCE + " You must be the bot owner."
        Permission.GUILD_OWNER -> messages.errors.MISSING_CLEARANCE + " You must be the guild owner."
        else -> ""
    }

    if (!permissionsService.hasClearance(member, requiredPermissionLevel))
        return@precondition Fail(response)

    return@precondition Pass
}