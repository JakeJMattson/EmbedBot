package me.jakejmattson.embedbot.commands

import com.google.gson.JsonSyntaxException
import me.jakejmattson.embedbot.arguments.EmbedArg
import me.jakejmattson.embedbot.dataclasses.CopyLocation
import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.locale.messages
import me.jakejmattson.embedbot.services.*
import me.jakejmattson.discordkt.api.annotations.CommandSet
import me.jakejmattson.discordkt.api.arguments.*
import me.jakejmattson.discordkt.api.dsl.command.*
import net.dv8tion.jda.api.entities.TextChannel

@CommandSet("Core")
fun coreCommands(embedService: EmbedService, permissionsService: PermissionsService) = commands {
    command("Send") {
        description = "Send the currently loaded embed."
        requiresLoadedEmbed = true
        execute(TextChannelArg("Channel").makeOptional { it.channel as TextChannel },
            BooleanArg("shouldTrack").makeOptional(false)) {
            val (channel, shouldTrack) = it.args
            val embed = it.guild!!.getLoadedEmbed()!!

            if (embed.isEmpty)
                return@execute it.respond(messages.EMPTY_EMBED)

            channel.sendMessage(embed.build()).queue { message ->
                if (shouldTrack)
                    embed.copyLocation = CopyLocation(channel.id, message.id)
            }

            if (channel != it.channel)
                it.reactSuccess()
        }
    }

    command("Create") {
        description = "Create a new embed with this name."
        execute(AnyArg("Embed Name")) {
            val embedName = it.args.first
            val wasCreated = embedService.createEmbed(it.guild!!, embedName)

            if (!wasCreated)
                return@execute it.respond(messages.EMBED_ALREADY_EXISTS)

            it.reactSuccess()
        }
    }

    command("Duplicate") {
        description = "Create a new embed from an existing embed."
        execute(AnyArg("Embed Name"), EmbedArg.makeNullableOptional { it.guild!!.getLoadedEmbed() }) {
            val embedName = it.args.first
            val existingEmbed = it.args.second
                ?: return@execute it.respond(messages.MISSING_OPTIONAL_EMBED)

            val embed = createEmbedFromJson(embedName, existingEmbed.toJson())
            val wasCreated = embedService.addEmbed(it.guild!!, embed)

            if (!wasCreated)
                it.respond(messages.EMBED_ALREADY_EXISTS)

            it.reactSuccess()
        }
    }

    command("Delete") {
        description = "Delete the embed with this name."
        execute(MultipleArg(EmbedArg).makeNullableOptional { it.guild!!.getLoadedEmbed()?.let { listOf(it) } }) { event ->
            val embeds = event.args.first
                ?: return@execute event.respond(messages.MISSING_OPTIONAL_EMBED)

            embeds.forEach {
                event.guild!!.removeEmbed(it)
            }

            event.reactSuccess()
        }
    }

    command("Load") {
        description = "Load the embed with this name into memory."
        execute(EmbedArg) {
            val embed = it.args.first
            it.guild!!.loadEmbed(embed)
            it.reactSuccess()
        }
    }

    command("Import") {
        description = "Import a JSON file or string as an embed."
        execute(AnyArg("Embed Name"), FileArg("File") or EveryArg("String")) {
            val (name, input) = it.args
            val json = input.map({ file -> file.readText().also { file.delete() } }, { it })

            it.importJson(name, json, embedService)
        }
    }

    command("Export") {
        description = "Export the currently loaded embed to JSON."
        execute(EmbedArg.makeNullableOptional { it.guild!!.getLoadedEmbed() }, BooleanArg("preferFile").makeOptional(false)) {
            val embed = it.args.first
                ?: return@execute it.respond(messages.MISSING_OPTIONAL_EMBED)

            val preferFile = it.args.second
            val json = embed.toJson()

            if (preferFile || json.length >= 1985)
                it.channel.sendFile(json.toByteArray(), "$${embed.name}.json").queue()
            else
                it.respond("```json\n$json```")
        }
    }

    command("ExecuteAll") {
        description = "Execute a batch of commands in sequence."
        execute(EveryArg("Commands")) { event ->
            event.respond("Command currently disabled")
            val rawInvocations = event.args.first.split("\n").filter { it.isNotEmpty() }
            val unknownCommands = mutableListOf<String>()
            val missingPermissions = mutableListOf<String>()

            val commandMap = rawInvocations.mapNotNull {
                val split = it.split(" ")
                val commandName = split.first()
                val command = event.container[commandName]

                if (command == null) {
                    unknownCommands.add(commandName)
                    return@mapNotNull null
                }

                val hasPermission = permissionsService.hasClearance(event.message.member!!, command.requiredPermissionLevel)

                if (!hasPermission) {
                    missingPermissions.add(commandName)
                    return@mapNotNull null
                }

                command to split.drop(1)
            }

            if (unknownCommands.isNotEmpty() || missingPermissions.isNotEmpty()) {
                val unknownResponse = when (unknownCommands.size) {
                    0 -> ""
                    1 -> "Unknown Command: ${unknownCommands.first()}"
                    else -> "Unknown Commands: ${unknownCommands.joinToString()}"
                }

                val permissionResponse = when (missingPermissions.size) {
                    0 -> ""
                    else -> "Missing Permissions: ${missingPermissions.joinToString()}"
                }

                val response =
                    if (unknownResponse.isNotEmpty() && permissionResponse.isNotEmpty())
                        unknownResponse + "\n" + permissionResponse
                    else
                        unknownResponse + permissionResponse

                return@execute event.respond(response)
            }

            if (commandMap.isEmpty())
                return@execute event.respond("No commands to execute!")
        }
    }
}

private fun CommandEvent<*>.importJson(name: String, json: String, embedService: EmbedService) {
    val guild = guild!!

    if (guild.hasEmbedWithName(name))
        return respond(messages.EMBED_ALREADY_EXISTS)

    try {
        val embed = createEmbedFromJson(name, json)
        val wasAdded = embedService.addEmbed(guild, embed)

        if (!wasAdded)
            respond(messages.EMBED_ALREADY_EXISTS)

        reactSuccess()
    } catch (e: JsonSyntaxException) {
        respond("Invalid JSON! ${e.message?.substringAfter("Exception: ")}")
    }
}