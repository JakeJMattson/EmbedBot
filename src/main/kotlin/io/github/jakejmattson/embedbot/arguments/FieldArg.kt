package io.github.jakejmattson.embedbot.arguments

import io.github.jakejmattson.embedbot.extensions.getLoadedEmbed
import io.github.jakejmattson.embedbot.services.Field
import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.aberrantfox.kjdautils.internal.command.*

open class FieldArg(override val name: String = "Field Data") : ArgumentType {
    companion object : FieldArg()

    override val examples = arrayListOf("Title|Body")
    override val consumptionType = ConsumptionType.Multiple
    override fun convert(arg: String, args: List<String>, event: CommandEvent): ArgumentResult {
        val guild = event.guild ?: return ArgumentResult.Error("Must be invoked in a guild!")
        val data = args.joinToString(" ").split("|")

        guild.getLoadedEmbed() ?: return ArgumentResult.Error("No embed loaded!")

        if (data.size !in 2..3)
            return ArgumentResult.Error("Invalid field data. Expected 2-3 items split by \"|\". Received ${data.size}")

        val field = Field(
            data.component1(),
            data.component2(),
            if (data.size == 3) data.component3().toBoolean() else false
        )

        return ArgumentResult.Multiple(field, args)
    }
}