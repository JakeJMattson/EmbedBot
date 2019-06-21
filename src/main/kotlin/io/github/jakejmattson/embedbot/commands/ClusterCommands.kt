package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.ClusterArg
import io.github.jakejmattson.embedbot.dataclasses.*
import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.*
import net.dv8tion.jda.core.entities.TextChannel

@CommandSet("Cluster")
fun clusterCommands(embedService: EmbedService) = commands {
    command("CreateCluster") {
        requiresGuild = true
        description = "Create a cluster for storing and deploying groups of embeds."
        expect(arg(WordArg("Cluster Name")))
        execute {
            val clusterName = it.args.component1() as String
            val wasCreated = embedService.createCluster(it.guild!!, clusterName)

            it.respond(
                if (wasCreated)
                    "Successfully created the cluster :: $clusterName"
                else
                    "A cluster with this name already exists."
            )
        }
    }

    command("DeleteCluster") {
        requiresGuild = true
        description = "Delete a cluster and all of its embeds."
        expect(ClusterArg)
        execute {
            val clusterName = it.args.component1() as Cluster
            val wasDeleted = embedService.deleteCluster(it.guild!!, clusterName)

            it.respond(
                if (wasDeleted)
                    "Successfully deleted the cluster :: $clusterName"
                else
                    "No such cluster with this name."
            )
        }
    }

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
            val embeds = ArrayList<Embed>()

            if (amount <= 0)
                return@execute it.respond("Cluster size should be 1 or greater.")

            var index = 1

            channel.iterableHistory.complete().takeWhile {
                val embed = it.getEmbed()

                if (embed != null) {
                    embeds.add(Embed("$clusterName-$index", embed.toEmbedBuilder(), CopyLocation(channel.id, it.id)))
                    index++
                }

                embeds.size != amount
            }

            embeds.reverse()
            embedService.createClusterFromEmbeds(it.guild!!, Cluster(clusterName, embeds))

            it.respond("Cloned ${embeds.size} embeds into $clusterName")
        }
    }

    command("Deploy") {
        requiresGuild = true
        description = "Deploy a cluster into a target channel."
        expect(arg(ClusterArg),
                arg(TextChannelArg("Channel"), optional = true, default = { it.channel }))
        execute {
            val name = it.args.component1() as String
            val channel = it.args.component2() as TextChannel
            val clusters = it.guild!!.getGuildClusters()

            val cluster = clusters.firstOrNull { it.name.toLowerCase() == name.toLowerCase() }
                ?: return@execute it.respond("No cluster found with that name.")

            cluster.embeds.filter { !it.isEmpty }.forEach {
                channel.sendMessage(it.build()).queue()
            }
        }
    }
}