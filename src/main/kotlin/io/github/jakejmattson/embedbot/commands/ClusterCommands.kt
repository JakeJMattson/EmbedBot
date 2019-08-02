package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.*
import io.github.jakejmattson.embedbot.dataclasses.*
import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.services.EmbedService
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.*
import net.dv8tion.jda.core.entities.TextChannel

@CommandSet("Cluster")
fun clusterCommands(embedService: EmbedService) = commands {
    command("CreateCluster") {
        description = "Create a cluster for storing and deploying groups of embeds."
        expect(arg(WordArg("Cluster Name")))
        execute {
            val clusterName = it.args.component1() as String
            val wasCreated = embedService.createCluster(it.guild!!, clusterName)

            val response =
                if (wasCreated)
                    "Successfully created the cluster :: $clusterName"
                else
                    "A cluster with this name already exists."

            it.respond(response)
        }
    }

    command("DeleteCluster") {
        description = "Delete a cluster and all of its embeds."
        expect(ClusterArg)
        execute {
            val cluster = it.args.component1() as Cluster
            val wasDeleted = embedService.deleteCluster(it.guild!!, cluster)

            val response =
                if (wasDeleted)
                    "Successfully deleted the cluster :: ${cluster.name}"
                else
                    "No such cluster with this name."

            it.respond(response)
        }
    }

    command("CloneCluster") {
        description = "Clone a group of embeds into a cluster."
        expect(arg(WordArg("Cluster Name")),
                arg(TextChannelArg("Channel"), optional = true, default = { it.channel }),
                arg(IntegerArg("Amount")))
        execute {
            val clusterName = it.args.component1() as String
            val channel = it.args.component2() as TextChannel
            val amount = it.args.component3() as Int

            if (amount <= 0)
                return@execute it.respond("Cluster size should be 1 or greater.")

            val messagesWithEmbeds = channel.iterableHistory.complete().filter { it.getEmbed() != null }.take(amount)
               as ArrayList

            messagesWithEmbeds.reverse()

            val embeds = messagesWithEmbeds.mapIndexed { index, message ->
                Embed("$clusterName-${index + 1}", message.getEmbed()!!.toEmbedBuilder(), CopyLocation(channel.id, message.id))
            } as ArrayList

            val wasSuccessful = embedService.createClusterFromEmbeds(it.guild!!, Cluster(clusterName, embeds))

            val response =
                if (wasSuccessful)
                    "Cloned ${embeds.size} embeds into $clusterName"
                else
                    "A cluster with that name already exists."

            it.respond(response)
        }
    }

    command("UpdateCluster") {
        description = "Update the original embeds this cluster was copied from."
        expect(ClusterArg)
        execute { event ->
            val cluster = event.args.component1() as Cluster
            val failures = ArrayList<String>()
            val size = cluster.size

            val totalSuccessful = cluster.embeds.sumBy {
                with(it.update(event.jda)) {
                    if (!canUpdate)
                        failures.add("${it.name} :: $reason")

                    if (canUpdate) 1 else 0
                }
            }

            if (totalSuccessful == size)
                return@execute event.respond("Successfully updated all $size embeds in ${cluster.name}")

            event.respond("Successfully updated $totalSuccessful out of $size in ${cluster.name}" +
                "\nFailed the following updates:\n${failures.joinToString("\n")}")
        }
    }

    command("RenameCluster") {
        description = "Change the name of an existing cluster."
        expect(ClusterArg, WordArg("New Name"))
        execute {
            val cluster = it.args.component1() as Cluster
            val newName = it.args.component2() as String

            if (it.guild!!.hasClusterWithName(newName))
                return@execute it.respond("An embed with this name already exists.")
            
            cluster.name = newName
            it.respond("Successfully changed the name of the cluster to: $newName")
        }
    }

    command("Deploy") {
        description = "Deploy a cluster into a target channel."
        expect(arg(ClusterArg),
                arg(TextChannelArg("Channel"), optional = true, default = { it.channel }),
                arg(BooleanArg("shouldTrack"), optional = true, default = false))
        execute {
            val cluster = it.args.component1() as Cluster
            val channel = it.args.component2() as TextChannel
            val shouldTrack = it.args.component3() as Boolean

            cluster.embeds.forEach { embed ->
                channel.sendMessage(embed.build()).queue { message ->
                    if (shouldTrack)
                        embed.copyLocation = CopyLocation(channel.id, message.id)
                }
            }
        }
    }

    command("AddToCluster") {
        description = "Add an embed into a cluster."
        expect(ClusterArg, EmbedArg)
        execute {
            val cluster = it.args.component1() as Cluster
            val embed = it.args.component2() as Embed

            cluster.addEmbed(it.guild!!, embed)

            it.respond("Successfully added ${embed.name} to ${cluster.name}")
        }
    }

    command("InsertIntoCluster") {
        description = "Insert an embed into a cluster at an index."
        expect(ClusterArg, IntegerArg("Index"), EmbedArg)
        execute {
            val cluster = it.args.component1() as Cluster
            val index = it.args.component2() as Int
            val embed = it.args.component3() as Embed

            if (index !in 0..cluster.size)
                return@execute it.respond("Invalid Index. Expected range: 0-${cluster.size}")

            cluster.insertEmbed(it.guild!!, index, embed)
            it.respond("Cluster inserted at index $index")
        }
    }

    command("RemoveFromCluster") {
        description = "Remove an embed from its cluster."
        expect(EmbedArg)
        execute {
            val embed = it.args.component1() as Embed

            embedService.removeEmbedFromCluster(it.guild!!, embed)

            it.respond("Successfully removed ${embed.name} from its cluster.")
        }
    }
}