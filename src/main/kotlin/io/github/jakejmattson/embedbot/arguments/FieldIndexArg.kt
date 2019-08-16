package io.github.jakejmattson.embedbot.arguments

import io.github.jakejmattson.embedbot.extensions.getLoadedEmbed
import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.aberrantfox.kjdautils.internal.command.*

open class FieldIndexArg(override val name: String = "Field Index") : ArgumentType {
    companion object : FieldIndexArg()

    override val examples = arrayListOf("0")
    override val consumptionType = ConsumptionType.Single
    override fun convert(arg: String, args: List<String>, event: CommandEvent): ArgumentResult {
        val guild = event.guild ?: return ArgumentResult.Error("Must be invoked in a guild!")

        val embed = guild.getLoadedEmbed()
            ?: return ArgumentResult.Error("No embed loaded!")

        if (embed.fieldCount == 0)
            return ArgumentResult.Error("This embed has no fields.")

        val index = arg.toIntOrNull()
            ?: return ArgumentResult.Error("Expected an integer, got $arg")

        if (index !in 0 until embed.fieldCount)
            return ArgumentResult.Error("Invalid index. Expected range: 0-${embed.fieldCount - 1}")

        return ArgumentResult.Single(index)
    }
}