package me.jakejmattson.embedbot.arguments

import me.jakejmattson.discordkt.api.dsl.arguments.*
import me.jakejmattson.discordkt.api.dsl.command.CommandEvent
import me.jakejmattson.embedbot.dataclasses.Group
import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.utils.messages

open class GroupArg(override val name: String = "Group") : ArgumentType<Group>() {
    companion object : GroupArg()

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Group> {
        val guild = event.guild ?: return Error(messages.MISSING_GUILD)

        val guildGroup = guild.getGroupByName(arg)
            ?: return Error("No such group exists with the name: $arg")

        return Success(guildGroup)
    }

    override fun generateExamples(event: CommandEvent<*>) = event.guild?.getGroups()?.map { it.name }
        ?: listOf("<No Groups>")

    override fun formatData(data: Group) = data.name
}