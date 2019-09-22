package io.github.jakejmattson.embedbot.services

import io.github.jakejmattson.embedbot.dataclasses.Configuration
import io.github.jakejmattson.embedbot.extensions.requiredPermissionLevel
import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.api.dsl.Command
import me.aberrantfox.kjdautils.discord.Discord
import me.aberrantfox.kjdautils.extensions.jda.toMember
import net.dv8tion.jda.api.entities.*

enum class Permission {
    BOT_OWNER,
    GUILD_OWNER,
    STAFF,
    NONE
}

val DEFAULT_REQUIRED_PERMISSION = Permission.STAFF

@Service
class PermissionsService(private val configuration: Configuration, discord: Discord) {
    init {
        discord.configuration.visibilityPredicate = { command: Command, user: User, _: MessageChannel, guild: Guild? ->
            if (guild != null) {
                val member = user.toMember(guild)!!
                val permission = command.requiredPermissionLevel

                hasClearance(member, permission)
            }
            else {
                false
            }
        }
    }

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