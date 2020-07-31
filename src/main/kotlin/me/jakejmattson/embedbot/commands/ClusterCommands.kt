package me.jakejmattson.embedbot.commands

import me.jakejmattson.discordkt.api.annotations.CommandSet
import me.jakejmattson.discordkt.api.arguments.*
import me.jakejmattson.discordkt.api.dsl.command.commands
import me.jakejmattson.discordkt.api.dsl.embed.toEmbedBuilder
import me.jakejmattson.embedbot.arguments.*
import me.jakejmattson.embedbot.dataclasses.*
import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.services.EmbedService
import me.jakejmattson.embedbot.utils.messages
import net.dv8tion.jda.api.entities.TextChannel

@CommandSet("Cluster")
fun clusterCommands(embedService: EmbedService) = commands {
    command("CreateCluster") {
        description = "Create a cluster for storing and deploying groups of embeds."
        execute(AnyArg("Cluster Name"), MultipleArg(EmbedArg).makeOptional(listOf())) {
            val (clusterName, embeds) = it.args
            val guild = it.guild!!
            val cluster = embedService.createCluster(guild, clusterName)
                ?: return@execute it.respond(messages.CLUSTER_ALREADY_EXISTS)

            embeds.forEach { embed ->
                cluster.addEmbed(guild, embed)
            }

            it.reactSuccess()
        }
    }

    command("DeleteCluster") {
        description = "Delete a cluster and all of its embeds."
        execute(ClusterArg) {
            val cluster = it.args.first
            val wasDeleted = embedService.deleteCluster(it.guild!!, cluster)

            if (!wasDeleted)
                it.respond("No such cluster with this name.")

            it.reactSuccess()
        }
    }

    command("CloneCluster") {
        description = "Clone a group of embeds into a cluster."
        execute(AnyArg("Cluster Name"),
            TextChannelArg("Channel").makeOptional { it.channel as TextChannel },
            IntegerArg("Amount")) { event ->
            val (clusterName, channel, amount) = event.args

            if (amount <= 0)
                return@execute event.respond("Cluster size should be 1 or greater.")

            val messagesWithEmbeds = channel.iterableHistory.complete().filter { it.getEmbed() != null }.take(amount)
                as ArrayList

            messagesWithEmbeds.reverse()

            val embeds = messagesWithEmbeds.mapIndexed { index, message ->
                Embed("$clusterName-${index + 1}", message.getEmbed()!!.toEmbedBuilder(), CopyLocation(channel.id, message.id))
            } as ArrayList

            val wasSuccessful = embedService.createClusterFromEmbeds(event.guild!!, Cluster(clusterName, embeds))

            if (!wasSuccessful)
                event.respond(messages.CLUSTER_ALREADY_EXISTS)

            event.respond("Cloned ${embeds.size} embeds into $clusterName")
        }
    }

    command("UpdateCluster") {
        description = "Update the original embeds this cluster was copied from."
        execute(ClusterArg) { event ->
            val cluster = event.args.first
            val failures = ArrayList<String>()
            val size = cluster.size

            val totalSuccessful = cluster.embeds.sumBy { embed ->

                val location = embed.copyLocation

                if (location == null) {
                    failures.add(messages.NOT_COPIED)
                    return@sumBy 0
                }

                val updateResponse = embed.update(event.discord.jda, location.channelId, location.messageId)

                with(updateResponse) {
                    if (!wasSuccessful)
                        failures.add("${embed.name} :: ${updateResponse.message}")

                    if (wasSuccessful) 1 else 0
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
        execute(ClusterArg, AnyArg("New Name")) {
            val (cluster, newName) = it.args

            if (it.guild!!.hasClusterWithName(newName))
                return@execute it.respond(messages.CLUSTER_ALREADY_EXISTS)

            cluster.name = newName
            it.reactSuccess()
        }
    }

    command("Deploy") {
        description = "Deploy a cluster into a target channel."
        execute(ClusterArg,
            TextChannelArg("Channel").makeOptional { it.channel as TextChannel },
            BooleanArg("shouldTrack").makeOptional(false)) {
            val (cluster, channel, shouldTrack) = it.args

            cluster.embeds.forEach { embed ->
                if (!embed.isEmpty) {
                    channel.sendMessage(embed.build()).queue { message ->
                        if (shouldTrack)
                            embed.copyLocation = CopyLocation(channel.id, message.id)
                    }
                }
            }

            if (channel != it.channel)
                it.reactSuccess()
        }
    }

    command("AddToCluster") {
        description = "Add an embed into a cluster."
        execute(ClusterArg, MultipleArg(EmbedArg)) {
            val (cluster, embeds) = it.args
            val guild = it.guild!!
            val additions = arrayListOf<String>()

            embeds.forEach { embed ->
                cluster.addEmbed(guild, embed)
                additions.add(embed.name)
            }

            it.reactSuccess()
        }
    }

    command("InsertIntoCluster") {
        description = "Insert an embed into a cluster at an index."
        execute(ClusterArg, IntegerArg("Index"), EmbedArg) {
            val (cluster, index, embed) = it.args

            if (index !in 0..cluster.size)
                return@execute it.respond("Invalid Index. Expected range: 0-${cluster.size}")

            cluster.insertEmbed(it.guild!!, index, embed)

            it.reactSuccess()
        }
    }

    command("RemoveFromCluster") {
        description = "Remove embeds from their current cluster."
        execute(MultipleArg(EmbedArg)) {
            val embeds = it.args.first
            val removals = arrayListOf<String>()

            embeds.forEach { embed ->
                embedService.removeEmbedFromCluster(it.guild!!, embed)
                removals.add(embed.name)
            }

            it.reactSuccess()
        }
    }
}