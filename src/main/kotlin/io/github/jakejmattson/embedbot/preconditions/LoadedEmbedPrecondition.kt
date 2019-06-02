package io.github.jakejmattson.embedbot.preconditions

import io.github.jakejmattson.embedbot.services.getLoadedEmbed
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.*

private val categoriesToApplyTo = arrayListOf("Clear", "Edit", "Field")

@Precondition
fun produceHasLoadedEmbedPrecondition() = exit@{ event: CommandEvent ->
    val command = event.container.commands[event.commandStruct.commandName] ?: return@exit Pass
    if (command.category !in categoriesToApplyTo) return@exit Pass

    val guild = event.guild ?: return@exit Fail("This can only be executed within a guild.")
    getLoadedEmbed(guild) ?: return@exit Fail("No embed loaded!")

    return@exit Pass
}