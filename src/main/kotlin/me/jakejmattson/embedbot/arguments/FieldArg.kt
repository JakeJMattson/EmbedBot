package me.jakejmattson.embedbot.arguments

import me.jakejmattson.discordkt.api.dsl.arguments.*
import me.jakejmattson.discordkt.api.dsl.command.CommandEvent
import me.jakejmattson.embedbot.extensions.getLoadedEmbed
import me.jakejmattson.embedbot.services.Field
import me.jakejmattson.embedbot.utils.*

open class FieldArg(override val name: String = "Field Data", private val delimiter: String = "|") : ArgumentType<Field>() {
    companion object : FieldArg()

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Field> {
        val guild = event.guild ?: return Error(messages.MISSING_GUILD)
        val data = args.joinToString(" ").split(delimiter)

        guild.getLoadedEmbed() ?: return Error(messages.MISSING_EMBED)

        if (data.size !in 2..3)
            return Error("Invalid field data. Expected 2-3 items split by \"$delimiter\". Received ${data.size}")

        val name = data.component1()
        val value = data.component2()
        val inline = if (data.size == 3) data.component3().toBoolean() else false

        if (name.length > FIELD_NAME_LIMIT)
            return Error("Max field name length is $FIELD_NAME_LIMIT characters. Input was ${name.length}.")

        if (value.length > FIELD_VALUE_LIMIT)
            return Error("Max field value length is $FIELD_VALUE_LIMIT characters. Input was ${value.length}.")

        val field = Field(name, value, inline)

        return Success(field, args.size)
    }

    override fun generateExamples(event: CommandEvent<*>) = listOf("Title|Body")
}