package me.jakejmattson.embedbot.commands

import com.google.gson.JsonSyntaxException
import me.jakejmattson.discordkt.api.annotations.CommandSet
import me.jakejmattson.discordkt.api.arguments.*
import me.jakejmattson.discordkt.api.dsl.command.*
import me.jakejmattson.discordkt.api.dsl.embed.toEmbedBuilder
import me.jakejmattson.discordkt.internal.command.ParseResult
import me.jakejmattson.embedbot.arguments.EmbedArg
import me.jakejmattson.embedbot.dataclasses.*
import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.services.*
import me.jakejmattson.embedbot.utils.messages
import net.dv8tion.jda.api.entities.TextChannel

@CommandSet("Core")
fun coreCommands(embedService: EmbedService, permissionsService: PermissionsService) = commands {
    command("Send") {
        description = "Send the currently loaded embed."
        requiresLoadedEmbed = true
        execute(TextChannelArg("Channel").makeOptional { it.channel as TextChannel },
            BooleanArg("saveLocation").makeOptional(false)) {
            val (channel, saveLocation) = it.args
            val embed = it.guild!!.getLoadedEmbed()!!

            if (embed.isEmpty)
                return@execute it.respond(messages.EMPTY_EMBED)

            channel.sendMessage(embed.build()).queue { message ->
                if (saveLocation)
                    embed.location = Location(channel.id, message.id)
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
            val existingEmbed = it.args.second ?: return@execute it.respond(messages.MISSING_OPTIONAL_EMBED)
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
            val embeds = event.args.first ?: return@execute event.respond(messages.MISSING_OPTIONAL_EMBED)

            embeds.forEach { event.guild!!.removeEmbed(it) }
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

    command("Copy") {
        description = "Copy an embed by its message ID."
        execute(AnyArg("Embed Name"), MessageArg("Message Link or ID")) {
            val (name, message) = it.args

            val guild = it.guild!!.takeIf { !it.hasEmbedWithName(name) }
                ?: return@execute it.respond(messages.EMBED_ALREADY_EXISTS)

            val builder = message.getEmbed()?.toEmbedBuilder()
                ?: return@execute it.respond("Target message has no embed.")

            val embed = Embed(name, builder, Location(message.channel.id, message.id))

            embedService.addEmbed(guild, embed)
            it.reactSuccess()
        }
    }

    command("Update") {
        description = "Update the message embed"
        requiresLoadedEmbed = true
        execute {
            val embed = it.guild!!.getLoadedEmbed()!!
            val original = embed.location ?: return@execute it.respond(messages.NO_LOCATION)
            val updateResponse = embed.update(it.discord, original.channelId, original.messageId)

            if (!updateResponse.wasSuccessful)
                return@execute it.respond(updateResponse.message)

            it.reactSuccess()
        }
    }

    command("UpdateTarget") {
        description = "Replace the target message embed with the loaded embed."
        requiresLoadedEmbed = true
        execute(MessageArg("Message Link or ID")) {
            val message = it.args.first
            val embed = it.guild!!.getLoadedEmbed()!!
            val updateResponse = embed.update(it.discord, message.channel.id, message.id)

            if (!updateResponse.wasSuccessful)
                return@execute it.respond(updateResponse.message)

            it.reactSuccess()
        }
    }

    command("ExecuteAll") {
        description = "Execute a batch of commands in sequence."
        execute(EveryArg("Commands")) { event ->
            val rawInvocations = event.args.first.split("\n").filter { it.isNotEmpty() }
            val invalidCommands = mutableListOf<String>()

            val commandMap = rawInvocations.mapNotNull {
                val split = it.split(" ")
                val commandName = split.first()
                val args = split.drop(1)

                event.container[commandName]?.let {
                    val hasPermission = permissionsService.hasClearance(event.message.member!!, it.requiredPermissionLevel)

                    if (hasPermission)
                        it to args
                    else
                        null
                }
            }

            if (invalidCommands.isNotEmpty())
                return@execute event.respond("Invalid: ${invalidCommands.joinToString()}")

            if (commandMap.isEmpty())
                return@execute event.respond("No Commands!")

            val genericEvent = event.cloneToGeneric()

            val (success, fail) = commandMap.map { (command, args) ->
                command to command.manualParseInput(args, genericEvent)
            }.partition { it.second is ParseResult.Success }

            if (fail.isNotEmpty()) {
                val response = fail.joinToString { it.first.names.first() }
                return@execute event.respond("Failed: $response")
            }

            println(success.joinToString { it.first.names.first() })

            success
                .map { it.first to (it.second as ParseResult.Success).argumentContainer }
                .forEach { (command, result) ->
                    println("${command.names.first()}(${result})")
                    command.manualInvoke(result, genericEvent)
                }

            event.reactSuccess()
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