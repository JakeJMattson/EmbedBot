package io.github.jakejmattson.embedbot.arguments

import io.github.jakejmattson.embedbot.services.getLoadedEmbed
import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.aberrantfox.kjdautils.internal.command.*

open class FieldIndexArg(override val name: String = "Field Index") : ArgumentType {
    companion object : FieldIndexArg()

    override val examples = arrayListOf("0")
    override val consumptionType = ConsumptionType.Single
    override fun convert(arg: String, args: List<String>, event: CommandEvent): ArgumentResult {
        val guild = event.guild ?: return ArgumentResult.Error("Must be invoked in a guild!")

        val embed = getLoadedEmbed(guild)
            ?: return ArgumentResult.Error("No embed loaded!")

        if (embed.isEmpty)
            return ArgumentResult.Error("This embed is empty.")

        val index = arg.toIntOrNull()
            ?: return ArgumentResult.Error("Expected an integer, got $arg")

        if (index !in 0..embed.lastFieldIndex)
            return ArgumentResult.Error("Invalid index. Expected range: 0-${embed.lastFieldIndex}")

        return ArgumentResult.Single(index)
    }
}