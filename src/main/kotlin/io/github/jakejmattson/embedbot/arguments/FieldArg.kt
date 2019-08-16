package io.github.jakejmattson.embedbot.arguments

import io.github.jakejmattson.embedbot.extensions.getLoadedEmbed
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.aberrantfox.kjdautils.internal.command.*

open class FieldArg(override val name: String = "Field Data", val delimiter: String = "|") : ArgumentType {
    companion object : FieldArg()

    override val examples = arrayListOf("Title|Body")
    override val consumptionType = ConsumptionType.Multiple
    override fun convert(arg: String, args: List<String>, event: CommandEvent): ArgumentResult {
        val guild = event.guild ?: return ArgumentResult.Error("Must be invoked in a guild!")
        val data = args.joinToString(" ").split(delimiter)

        guild.getLoadedEmbed() ?: return ArgumentResult.Error("No embed loaded!")

        if (data.size !in 2..3)
            return ArgumentResult.Error("Invalid field data. Expected 2-3 items split by \"$delimiter\". Received ${data.size}")

        val name = data.component1()
        val value = data.component2()
        val inline = if (data.size == 3) data.component3().toBoolean() else false

        if (name.length > FIELD_NAME_LIMIT)
            return ArgumentResult.Error("Max field name length is $FIELD_NAME_LIMIT characters. Input was ${name.length}.")

        if (value.length > FIELD_VALUE_LIMIT)
            return ArgumentResult.Error("Max field value length is $FIELD_VALUE_LIMIT characters. Input was ${value.length}.")

        val field = Field(name, value, inline)

        return ArgumentResult.Multiple(field, args)
    }
}