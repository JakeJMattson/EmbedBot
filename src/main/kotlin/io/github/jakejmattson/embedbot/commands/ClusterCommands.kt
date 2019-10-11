package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.*
import io.github.jakejmattson.embedbot.dataclasses.*
import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.locale.messages
import io.github.jakejmattson.embedbot.services.EmbedService
import me.aberrantfox.kjdautils.api.dsl.command.*
import me.aberrantfox.kjdautils.api.dsl.toEmbedBuilder
import me.aberrantfox.kjdautils.internal.arguments.*
import net.dv8tion.jda.api.entities.TextChannel

@CommandSet("Cluster")
fun clusterCommands(embedService: EmbedService) = commands {
    command("CreateCluster") {
        description = messages.descriptions.CREATE_CLUSTER
        execute(WordArg("Cluster Name"), MultipleArg(EmbedArg).makeOptional(listOf())) {
            val (clusterName, embeds) = it.args
            val guild = it.guild!!
            val cluster = embedService.createCluster(guild, clusterName)
                ?: return@execute it.respond(messages.errors.CLUSTER_ALREADY_EXISTS)

            embeds.forEach { embed ->
                cluster.addEmbed(guild, embed)
            }

            it.reactSuccess()
        }
    }

    command("DeleteCluster") {
        description = messages.descriptions.DELETE_CLUSTER
        execute(ClusterArg) {
            val cluster = it.args.component1()
            val wasDeleted = embedService.deleteCluster(it.guild!!, cluster)

            if (!wasDeleted)
                it.respond(messages.errors.NO_SUCH_CLUSTER)

            it.reactSuccess()
        }
    }

    command("CloneCluster") {
        description = messages.descriptions.CLONE_CLUSTER
        execute(WordArg("Cluster Name"),
            TextChannelArg("Channel").makeOptional { it.channel as TextChannel },
            IntegerArg("Amount")) { event ->
            val (clusterName, channel, amount) = event.args

            if (amount <= 0)
                return@execute event.respond(messages.errors.INVALID_CLUSTER_SIZE)

            val messagesWithEmbeds = channel.iterableHistory.complete().filter { it.getEmbed() != null }.take(amount)
                as ArrayList

            messagesWithEmbeds.reverse()

            val embeds = messagesWithEmbeds.mapIndexed { index, message ->
                Embed("$clusterName-${index + 1}", message.getEmbed()!!.toEmbedBuilder(), CopyLocation(channel.id, message.id))
            } as ArrayList

            val wasSuccessful = embedService.createClusterFromEmbeds(event.guild!!, Cluster(clusterName, embeds))

            if (!wasSuccessful)
                event.respond(messages.errors.CLUSTER_ALREADY_EXISTS)

            event.respond("Cloned ${embeds.size} embeds into $clusterName")
        }
    }

    command("UpdateCluster") {
        description = messages.descriptions.UPDATE_CLUSTER
        execute(ClusterArg) { event ->
            val cluster = event.args.component1()
            val failures = ArrayList<String>()
            val size = cluster.size

            val totalSuccessful = cluster.embeds.sumBy { embed ->

                val location = embed.copyLocation

                if (location == null) {
                    failures.add(messages.errors.NOT_COPIED)
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
        description = messages.descriptions.RENAME_CLUSTER
        execute(ClusterArg, WordArg("New Name")) {
            val (cluster, newName) = it.args

            if (it.guild!!.hasClusterWithName(newName))
                return@execute it.respond(messages.errors.CLUSTER_ALREADY_EXISTS)

            cluster.name = newName
            it.reactSuccess()
        }
    }

    command("Deploy") {
        description = messages.descriptions.DEPLOY
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
        description = messages.descriptions.ADD_TO_CLUSTER
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
        description = messages.descriptions.INSERT_INTO_CLUSTER
        execute(ClusterArg, IntegerArg("Index"), EmbedArg) {
            val (cluster, index, embed) = it.args

            if (index !in 0..cluster.size)
                return@execute it.respond("Invalid Index. Expected range: 0-${cluster.size}")

            cluster.insertEmbed(it.guild!!, index, embed)

            it.reactSuccess()
        }
    }

    command("RemoveFromCluster") {
        description = messages.descriptions.REMOVE_FROM_CLUSTER
        execute(MultipleArg(EmbedArg)) {
            val embeds = it.args.component1()
            val removals = arrayListOf<String>()

            embeds.forEach { embed ->
                embedService.removeEmbedFromCluster(it.guild!!, embed)
                removals.add(embed.name)
            }

            it.reactSuccess()
        }
    }
}