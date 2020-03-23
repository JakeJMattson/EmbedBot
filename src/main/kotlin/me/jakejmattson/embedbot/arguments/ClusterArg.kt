package me.jakejmattson.embedbot.arguments

import me.aberrantfox.kjdautils.api.dsl.command.CommandEvent
import me.aberrantfox.kjdautils.internal.command.*
import me.jakejmattson.embedbot.dataclasses.Cluster
import me.jakejmattson.embedbot.extensions.getClusterByName
import me.jakejmattson.embedbot.locale.messages

open class ClusterArg(override val name: String = "Cluster") : ArgumentType<Cluster>() {
    companion object : ClusterArg()

    override val examples = arrayListOf("")
    override val consumptionType = ConsumptionType.Single
    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Cluster> {
        val guild = event.guild ?: return ArgumentResult.Error(messages.errors.MISSING_GUILD)

        val guildCluster = guild.getClusterByName(arg)
            ?: return ArgumentResult.Error("No such cluster exists with the name: $arg")

        return ArgumentResult.Success(guildCluster)
    }
}