package io.github.jakejmattson.embedbot.arguments

import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.aberrantfox.kjdautils.internal.command.*

open class ClusterArg(override val name: String = "Cluster Name") : ArgumentType {
    companion object : ClusterArg()

    override val examples = arrayListOf("")
    override val consumptionType = ConsumptionType.Single
    override fun convert(arg: String, args: List<String>, event: CommandEvent): ArgumentResult {
        val guild = event.guild ?: return ArgumentResult.Error("Must be invoked inside a guild.")

        val guildCluster = guild.getGuildClusters().firstOrNull { it.name.toLowerCase() == arg.toLowerCase() }
            ?: return ArgumentResult.Error("No such cluster exists with the name: $arg")

        return ArgumentResult.Single(guildCluster)
    }
}