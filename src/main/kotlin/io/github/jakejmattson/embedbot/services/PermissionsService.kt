package io.github.jakejmattson.embedbot.services

import io.github.jakejmattson.embedbot.dataclasses.Configuration
import me.aberrantfox.kjdautils.api.annotation.Service
import net.dv8tion.jda.api.entities.Member

enum class Permission {
    BOT_OWNER,
    GUILD_OWNER,
    STAFF,
    NONE
}

val DEFAULT_REQUIRED_PERMISSION = Permission.STAFF

@Service
class PermissionsService(private val configuration: Configuration) {

    fun hasClearance(member: Member, requiredPermissionLevel: Permission) = member.getPermissionLevel().ordinal <= requiredPermissionLevel.ordinal

    private fun Member.getPermissionLevel() =
        when {
            isBotOwner() -> Permission.BOT_OWNER
            isGuildOwner() -> Permission.GUILD_OWNER
            isStaff() -> Permission.STAFF
            else -> Permission.NONE
        }

    private fun Member.isBotOwner() = user.id == configuration.botOwner
    private fun Member.isGuildOwner() = isOwner
    private fun Member.isStaff(): Boolean {
        val guildConfig = configuration.getGuildConfig(guild.id) ?: return false
        val requiredRoleName = guildConfig.requiredRole
        val requiredRole = guild.getRolesByName(requiredRoleName, true).firstOrNull() ?: return false

        return requiredRole in roles
    }
}