package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.*
import io.github.jakejmattson.embedbot.dataclasses.*
import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.locale.messages
import io.github.jakejmattson.embedbot.services.EmbedService
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.arguments.*
import net.dv8tion.jda.api.entities.TextChannel

@CommandSet("Cluster")
fun clusterCommands(embedService: EmbedService) = commands {
    command("CreateCluster") {
        description = messages.descriptions.CREATE_CLUSTER
        expect(arg(WordArg("Cluster Name")), arg(MultipleArg(EmbedArg), optional = true, default = listOf<Embed>()))
        execute {
            val clusterName = it.args.component1() as String
            val embeds = it.args.component2() as List<Embed>
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
        expect(ClusterArg)
        execute {
            val cluster = it.args.component1() as Cluster
            val wasDeleted = embedService.deleteCluster(it.guild!!, cluster)

            if (!wasDeleted)
                it.respond(messages.errors.NO_SUCH_CLUSTER)

            it.reactSuccess()
        }
    }

    command("CloneCluster") {
        description = messages.descriptions.CLONE_CLUSTER
        expect(arg(WordArg("Cluster Name")),
                arg(TextChannelArg("Channel"), optional = true, default = { it.channel }),
                arg(IntegerArg("Amount")))
        execute { event ->
            val clusterName = event.args.component1() as String
            val channel = event.args.component2() as TextChannel
            val amount = event.args.component3() as Int

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
                event.respond(messages.errors.CLUSTER_ALREADY_EXISTS)

            event.respond("Cloned ${embeds.size} embeds into $clusterName")
        }
    }

    command("UpdateCluster") {
        description = messages.descriptions.UPDATE_CLUSTER
        expect(ClusterArg)
        execute { event ->
            val cluster = event.args.component1() as Cluster
            val failures = ArrayList<String>()
            val size = cluster.size

            val totalSuccessful = cluster.embeds.sumBy { embed ->

                val location = embed.copyLocation

                if (location == null) {
                    failures.add(messages.errors.NOT_COPIED)
                    return@sumBy 0
                }

                val updateResponse = embed.update(event.discord.jda, location.channelId, location.messageId)

                with (updateResponse) {
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
        expect(ClusterArg, WordArg("New Name"))
        execute {
            val cluster = it.args.component1() as Cluster
            val newName = it.args.component2() as String

            if (it.guild!!.hasClusterWithName(newName))
                return@execute it.respond(messages.errors.CLUSTER_ALREADY_EXISTS)
            
            cluster.name = newName
            it.reactSuccess()
        }
    }

    command("Deploy") {
        description = messages.descriptions.DEPLOY
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

            if (channel != it.channel)
                it.reactSuccess()
        }
    }

    command("AddToCluster") {
        description = messages.descriptions.ADD_TO_CLUSTER
        expect(ClusterArg, MultipleArg(EmbedArg))
        execute {
            val cluster = it.args.component1() as Cluster
            val embeds = it.args.component2() as List<Embed>
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
        expect(ClusterArg, IntegerArg("Index"), EmbedArg)
        execute {
            val cluster = it.args.component1() as Cluster
            val index = it.args.component2() as Int
            val embed = it.args.component3() as Embed

            if (index !in 0..cluster.size)
                return@execute it.respond("Invalid Index. Expected range: 0-${cluster.size}")

            cluster.insertEmbed(it.guild!!, index, embed)

            it.reactSuccess()
        }
    }

    command("RemoveFromCluster") {
        description = messages.descriptions.REMOVE_FROM_CLUSTER
        expect(MultipleArg(EmbedArg))
        execute {
            val embeds = it.args.component1() as List<Embed>
            val removals = arrayListOf<String>()

            embeds.forEach { embed ->
                embedService.removeEmbedFromCluster(it.guild!!, embed)
                removals.add(embed.name)
            }

            it.reactSuccess()
        }
    }
}