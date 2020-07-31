package me.jakejmattson.embedbot.arguments

import me.jakejmattson.discordkt.api.dsl.arguments.*
import me.jakejmattson.discordkt.api.dsl.command.CommandEvent
import me.jakejmattson.embedbot.dataclasses.Cluster
import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.utils.messages

open class ClusterArg(override val name: String = "Cluster") : ArgumentType<Cluster>() {
    companion object : ClusterArg()

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Cluster> {
        val guild = event.guild ?: return Error(messages.MISSING_GUILD)

        val guildCluster = guild.getClusterByName(arg)
            ?: return Error("No such cluster exists with the name: $arg")

        return Success(guildCluster)
    }

    override fun generateExamples(event: CommandEvent<*>) = event.guild?.getClusters()?.map { it.name }
        ?: listOf("<No Clusters>")

    override fun formatData(data: Cluster) = data.name
}