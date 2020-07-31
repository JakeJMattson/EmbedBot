package me.jakejmattson.embedbot.arguments

import me.jakejmattson.discordkt.api.dsl.arguments.*
import me.jakejmattson.discordkt.api.dsl.command.CommandEvent
import me.jakejmattson.embedbot.dataclasses.Embed
import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.utils.messages

open class EmbedArg(override val name: String = "Embed") : ArgumentType<Embed>() {
    companion object : EmbedArg()

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Embed> {
        val guild = event.guild ?: return Error(messages.MISSING_GUILD)

        val embed = guild.getEmbedByName(arg)
            ?: return Error("No such embed exists with the name: $arg")

        return Success(embed)
    }

    override fun generateExamples(event: CommandEvent<*>) = event.guild?.getEmbeds()?.map { it.name }
        ?: listOf("<No Embeds>")

    override fun formatData(data: Embed) = data.name
}