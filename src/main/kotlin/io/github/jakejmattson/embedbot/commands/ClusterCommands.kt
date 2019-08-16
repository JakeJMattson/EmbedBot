package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.*
import io.github.jakejmattson.embedbot.dataclasses.*
import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.services.EmbedService
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.arguments.*
import net.dv8tion.jda.api.entities.TextChannel

@CommandSet("Cluster")
fun clusterCommands(embedService: EmbedService) = commands {
    command("CreateCluster") {
        description = "Create a cluster for storing and deploying groups of embeds."
        expect(arg(WordArg("Cluster Name")), arg(MultipleArg(EmbedArg), optional = true, default = listOf<Embed>()))
        execute {
            val clusterName = it.args.component1() as String
            val embeds = it.args.component2() as List<Embed>
            val guild = it.guild!!
            val cluster = embedService.createCluster(guild, clusterName)
                ?: return@execute it.respond("A cluster with this name already exists.")

            embeds.forEach {
                cluster.addEmbed(guild, it)
            }

            it.respondSuccess("Successfully created the cluster :: $clusterName")
        }
    }

    command("DeleteCluster") {
        description = "Delete a cluster and all of its embeds."
        expect(ClusterArg)
        execute {
            val cluster = it.args.component1() as Cluster
            val wasDeleted = embedService.deleteCluster(it.guild!!, cluster)

            if (!wasDeleted)
                it.respond("No such cluster with this name.")

            it.respondSuccess("Successfully deleted the cluster :: ${cluster.name}")
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

            if (!wasSuccessful)
                it.respond("A cluster with that name already exists.")

            it.respondSuccess("Cloned ${embeds.size} embeds into $clusterName")
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
                with(it.update(event.discord.jda)) {
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
            it.respondSuccess("Successfully changed the name of the cluster to: $newName")
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
                if (!embed.isEmpty) {
                    channel.sendMessage(embed.build()).queue { message ->
                        if (shouldTrack)
                            embed.copyLocation = CopyLocation(channel.id, message.id)
                    }
                }
            }
        }
    }

    command("AddToCluster") {
        description = "Add an embed into a cluster."
        expect(ClusterArg, MultipleArg(EmbedArg))
        execute {
            val cluster = it.args.component1() as Cluster
            val embeds = it.args.component2() as List<Embed>
            val guild = it.guild!!
            val additions = arrayListOf<String>()

            embeds.forEach {
                cluster.addEmbed(guild, it)
                additions.add(it.name)
            }

            it.respondSuccess("Successfully added the following embeds to ${cluster.name}:\n${additions.joinToString()}")
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
            it.respondSuccess("Cluster inserted at index $index")
        }
    }

    command("RemoveFromCluster") {
        description = "Remove embeds from their current cluster."
        expect(MultipleArg(EmbedArg))
        execute {
            val embeds = it.args.component1() as List<Embed>
            val removals = arrayListOf<String>()

            embeds.forEach { embed ->
                embedService.removeEmbedFromCluster(it.guild!!, embed)
                removals.add(embed.name)
            }

            it.respondSuccess("Successfully removed the following embeds from their cluster:\n${removals.joinToString()}")
        }
    }
}