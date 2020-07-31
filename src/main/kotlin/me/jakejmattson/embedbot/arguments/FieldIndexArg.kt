package me.jakejmattson.embedbot.arguments

import me.jakejmattson.discordkt.api.dsl.arguments.*
import me.jakejmattson.discordkt.api.dsl.command.CommandEvent
import me.jakejmattson.embedbot.extensions.getLoadedEmbed
import me.jakejmattson.embedbot.utils.messages

open class FieldIndexArg(override val name: String = "Field Index") : ArgumentType<Int>() {
    companion object : FieldIndexArg()

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Int> {
        val guild = event.guild ?: return Error(messages.MISSING_GUILD)

        val embed = guild.getLoadedEmbed()
            ?: return Error(messages.MISSING_EMBED)

        if (embed.fieldCount == 0)
            return Error("This embed has no fields.")

        val index = arg.toIntOrNull()
            ?: return Error("Expected an integer, got $arg")

        if (index !in 0 until embed.fieldCount)
            return Error("Invalid index. Expected range: 0-${embed.fieldCount - 1}")

        return Success(index)
    }

    override fun generateExamples(event: CommandEvent<*>): List<String> {
        val maxIndex = event.guild?.getLoadedEmbed()?.fieldCount ?: 0
        return listOf((0..maxIndex).random().toString())
    }
}