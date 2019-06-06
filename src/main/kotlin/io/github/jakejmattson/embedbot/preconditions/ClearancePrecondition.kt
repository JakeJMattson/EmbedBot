package io.github.jakejmattson.embedbot.preconditions

import io.github.jakejmattson.embedbot.data.Configuration
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.aberrantfox.kjdautils.internal.command.*

@Precondition
fun produceHasClearancePrecondition(configuration: Configuration) = exit@{ event: CommandEvent ->
    val guild = event.guild
        ?: return@exit Fail("This can only be executed within a guild.")

    val member = event.author.toMember(guild)

    if (member.isOwner)
        return@exit Pass

    val guildConfig = configuration.getGuildConfig(guild.id)
        ?: return@exit Fail("This guild is not configured for use.")

    val requiredRoleName = guildConfig.requiredRole

    val requiredRole = guild.getRolesByName(requiredRoleName, true).firstOrNull()
        ?: return@exit Fail("Guild missing the role defined in the configuration :: $requiredRoleName")

    if (requiredRole !in member.roles)
        return@exit Fail("Missing clearance to use this command.")

    return@exit Pass
}
