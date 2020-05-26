package me.jakejmattson.embedbot.arguments

import me.aberrantfox.kjdautils.api.dsl.command.CommandEvent
import me.aberrantfox.kjdautils.internal.command.*
import me.jakejmattson.embedbot.extensions.getLoadedEmbed
import me.jakejmattson.embedbot.locale.messages

open class FieldIndexArg(override val name: String = "Field Index") : ArgumentType<Int>() {
    companion object : FieldIndexArg()

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

    override fun generateExamples(event: CommandEvent<*>): MutableList<String> {
        val maxIndex = event.guild?.getLoadedEmbed()?.fieldCount ?: 0
        return mutableListOf((0..maxIndex).random().toString())
    }
}