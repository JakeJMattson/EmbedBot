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

@CommandSet("Group")
fun groupCommands(embedService: EmbedService) = commands {
    command("CreateGroup") {
        description = "Create a group of embeds."
        execute(AnyArg("Group Name"), MultipleArg(EmbedArg).makeOptional(listOf())) {
            val (groupName, embeds) = it.args
            val guild = it.guild!!
            val group = embedService.createGroup(guild, groupName)
                ?: return@execute it.respond(messages.GROUP_ALREADY_EXISTS)

            embeds.forEach { embed ->
                group.addEmbed(guild, embed)
            }

            it.reactSuccess()
        }
    }

    command("DeleteGroup") {
        description = "Delete a group and all of its embeds."
        execute(GroupArg) {
            val group = it.args.first
            val wasDeleted = embedService.deleteGroup(it.guild!!, group)

            if (!wasDeleted)
                it.respond("No such group with this name.")

            it.reactSuccess()
        }
    }

    command("CloneGroup") {
        description = "Clone a group of embeds."
        execute(AnyArg("Group Name"),
            TextChannelArg("Channel").makeOptional { it.channel as TextChannel },
            IntegerArg("Amount")) { event ->
            val (groupName, channel, amount) = event.args

            if (amount <= 0)
                return@execute event.respond("Group size should be 1 or greater.")

            val embeds =
                channel.iterableHistory.complete()
                    .filter { it.getEmbed() != null }
                    .take(amount)
                    .reversed()
                    .mapIndexed { index, message ->
                        Embed("$groupName-${index + 1}", message.getEmbed()!!.toEmbedBuilder(), Location(channel.id, message.id))
                    } as MutableList

            val wasSuccessful = embedService.createGroupFromEmbeds(event.guild!!, Group(groupName, embeds))

            if (!wasSuccessful)
                event.respond(messages.GROUP_ALREADY_EXISTS)

            event.respond("Cloned ${embeds.size} embeds into $groupName")
        }
    }

    command("UpdateGroup") {
        description = "Update the original embeds this group was copied from."
        execute(GroupArg) { event ->
            val group = event.args.first
            val failures = mutableListOf<String>()
            val size = group.size

            val totalSuccessful = group.embeds.sumBy { embed ->
                val location = embed.location

                if (location == null) {
                    failures.add(messages.NO_LOCATION)
                    return@sumBy 0
                }

                val updateResponse = embed.update(event.discord, location.channelId, location.messageId)

                with(updateResponse) {
                    if (!wasSuccessful)
                        failures.add("${embed.name} :: ${updateResponse.message}")

                    if (wasSuccessful) 1 else 0
                }
            }

            if (totalSuccessful == size)
                return@execute event.respond("Successfully updated all $size embeds in ${group.name}")

            event.respond("Successfully updated $totalSuccessful out of $size in ${group.name}" +
                "\nFailed the following updates:\n${failures.joinToString("\n")}")
        }
    }

    command("RenameGroup") {
        description = "Change the name of an existing group."
        execute(GroupArg, AnyArg("New Name")) {
            val (group, newName) = it.args

            if (it.guild!!.hasGroupWithName(newName))
                return@execute it.respond(messages.GROUP_ALREADY_EXISTS)

            group.name = newName
            it.reactSuccess()
        }
    }

    command("Deploy") {
        description = "Deploy a group into a target channel."
        execute(GroupArg,
            TextChannelArg("Channel").makeOptional { it.channel as TextChannel },
            BooleanArg("saveLocation").makeOptional(false)) {
            val (group, channel, saveLocation) = it.args

            group.embeds.forEach { embed ->
                if (!embed.isEmpty) {
                    channel.sendMessage(embed.build()).queue { message ->
                        if (saveLocation)
                            embed.location = Location(channel.id, message.id)
                    }
                }
            }

            if (channel != it.channel)
                it.reactSuccess()
        }
    }

    command("AddToGroup") {
        description = "Add an embed into a group."
        execute(GroupArg, MultipleArg(EmbedArg)) {
            val (group, embeds) = it.args
            val guild = it.guild!!

            embeds.forEach { embed ->
                group.addEmbed(guild, embed)
            }

            it.reactSuccess()
        }
    }

    command("InsertIntoGroup") {
        description = "Insert an embed into a group at an index."
        execute(GroupArg, IntegerArg("Index"), EmbedArg) {
            val (group, index, embed) = it.args

            if (index !in 0..group.size)
                return@execute it.respond("Invalid Index. Expected range: 0-${group.size}")

            group.addEmbed(it.guild!!, embed, index)

            it.reactSuccess()
        }
    }

    command("RemoveFromGroup") {
        description = "Remove embeds from their current group."
        execute(MultipleArg(EmbedArg)) {
            val embeds = it.args.first

            embeds.forEach { embed ->
                embedService.removeEmbedFromGroup(it.guild!!, embed)
            }

            it.reactSuccess()
        }
    }
}