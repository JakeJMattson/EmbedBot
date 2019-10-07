package io.github.jakejmattson.embedbot.arguments

import io.github.jakejmattson.embedbot.extensions.getLoadedEmbed
import io.github.jakejmattson.embedbot.locale.messages
import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.aberrantfox.kjdautils.internal.command.*

open class FieldIndexArg(override val name: String = "Field Index"): ArgumentType<Int>() {
    companion object : FieldIndexArg()

    override val examples = arrayListOf("0")
    override val consumptionType = ConsumptionType.Single
    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Int> {
        val guild = event.guild ?: return ArgumentResult.Error(messages.errors.MISSING_GUILD)

        val embed = guild.getLoadedEmbed()
            ?: return ArgumentResult.Error(messages.errors.MISSING_EMBED)

        if (embed.fieldCount == 0)
            return ArgumentResult.Error(messages.errors.NO_FIELDS)

        val index = arg.toIntOrNull()
            ?: return ArgumentResult.Error("Expected an integer, got $arg")

        if (index !in 0 until embed.fieldCount)
            return ArgumentResult.Error("Invalid index. Expected range: 0-${embed.fieldCount - 1}")

        return ArgumentResult.Success(index)
    }
}