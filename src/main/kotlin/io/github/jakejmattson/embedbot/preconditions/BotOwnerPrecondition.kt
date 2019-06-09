package io.github.jakejmattson.embedbot.preconditions

import io.github.jakejmattson.embedbot.data.Configuration
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.aberrantfox.kjdautils.internal.command.*
import net.dv8tion.jda.core.entities.Member

@Precondition
fun produceIsBotOwnerPrecondition(configuration: Configuration) = exit@{ event: CommandEvent ->
    val category = event.container.commands[event.commandStruct.commandName]?.category ?: return@exit Pass

    val guild = event.guild
        ?: return@exit Fail("This can only be executed within a guild.")

    val member = event.author.toMember(guild)
    fun Member.isBotOwner() = configuration.botOwner == this.user.id

    if (!member.isBotOwner() && category == "BotConfiguration")
        return@exit Fail("Missing clearance to use this command. You must be the bot owner.")

    return@exit Pass
}

