package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.*
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.TextChannel

@CommandSet("Cluster")
fun clusterCommands(embedService: EmbedService) = commands {
    command("CloneCluster") {
        requiresGuild = true
        description = "Clone a group of embeds into a cluster."
        expect(arg(WordArg("Cluster Name")),
                arg(TextChannelArg("Channel"), optional = true, default = { it.channel }),
                arg(IntegerArg("Amount")))
        execute {
            val clusterName = it.args.component1() as String
            val channel = it.args.component2() as TextChannel
            val amount = it.args.component3() as Int
            val embeds = ArrayList<EmbedBuilder>()

            if (amount <= 0)
                return@execute it.respond("Cluster size should be 1 or greater.")

            channel.iterableHistory.complete().takeWhile {
                val embed = it.getEmbed()

                if (embed != null)
                    embeds.add(embed.toEmbedBuilder())

                embeds.size != amount
            }

            embeds.reverse()
            embedService.createCluster(it.guild!!, GuildCluster(clusterName, embeds))

            it.respond("Cloned ${embeds.size} embeds into $clusterName")
        }
    }

    command("Deploy") {
        requiresGuild = true
        description = "Deploy a cluster into a target channel."
        expect(arg(WordArg("Cluster Name")),
                arg(TextChannelArg("Channel"), optional = true, default = { it.channel }))
        execute {
            val name = it.args.component1() as String
            val channel = it.args.component2() as TextChannel
            val clusters = it.guild!!.getGuildClusters()

            val cluster = clusters.firstOrNull { it.name.toLowerCase() == name.toLowerCase() }
                ?: return@execute it.respond("No cluster found with that name.")

            cluster.embeds.forEach {
                channel.sendMessage(it.build()).queue()
            }
        }
    }
}