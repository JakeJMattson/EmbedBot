package io.github.jakejmattson.embedbot.commands

import com.google.gson.GsonBuilder
import io.github.jakejmattson.embedbot.arguments.EmbedArg
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.extensions.stdlib.trimToID
import me.aberrantfox.kjdautils.internal.command.arguments.*
import me.aberrantfox.kjdautils.internal.command.tryRetrieveSnowflake
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.*
import java.lang.Exception

@CommandSet("Core")
fun coreCommands(embedService: EmbedService) = commands {
    val gson = GsonBuilder().setPrettyPrinting().create()

    command("Send") {
        requiresGuild = true
        description = "Send the currently loaded embed."
        execute {
            val embed = embedService.getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            val builder = embed.builder.takeIf { !it.isEmpty }
                ?:return@execute it.respond("This embed is empty.")

            it.respond(builder.build())
        }
    }

    command("Create") {
        requiresGuild = true
        description = "Create a new embed with this name."
        expect(WordArg("Embed Name"))
        execute {
            val embedName = it.args.component1() as String

            val wasCreated = embedService.createEmbed(it.guild!!, embedName)

            it.respond(
                if (wasCreated)
                    "Successfully added the embed: $embedName"
                else
                    "An embed with this name already exists"
            )
        }
    }

    command("Delete") {
        requiresGuild = true
        description = "Delete the embed with this name."
        expect(arg(EmbedArg, optional = true, default = {embedService.getLoadedEmbed(it.guild!!) as Any}))
        execute {
            val embed = it.args.component1() as Embed
            val wasRemoved = embedService.removeEmbed(it.guild!!, embed)

            it.respond(
                if (wasRemoved)
                    "Successfully removed the embed: ${embed.name}"
                else
                    "No such embed exists"
            )
        }
    }

    command("Load") {
        requiresGuild = true
        description = "Load the embed with this name into memory."
        expect(EmbedArg)
        execute {
            val embed = it.args.component1() as Embed
            embedService.loadEmbed(it.guild!!, embed)
            it.respond("Successfully loaded the embed: ${embed.name}")
        }
    }

    command("ListEmbeds") {
        requiresGuild = true
        description = "List all embeds created in this guild."
        execute {
            it.respond(embedService.listEmbeds(it.guild!!).takeIf { it.isNotEmpty() } ?: "<No embeds>")
        }
    }

    command("Copy") {
        requiresGuild = true
        description = "Copy an embed by its message ID."
        expect(arg(WordArg("Embed Name")), arg(TextChannelArg, optional = true, default = { it.channel }), arg(WordArg("Message ID")))
        execute {
            val name = it.args.component1() as String
            val channel = it.args.component2() as TextChannel
            val messageId = it.args.component3() as String
            val guild = it.guild!!

            if (guild.hasEmbedWithName(name))
                return@execute it.respond("An embed with this name already exists")

            val message = tryRetrieveSnowflake(it.jda) {
                channel.getMessageById(messageId.trimToID()).complete()
            } as Message? ?: return@execute it.respond("Could not find a message with that ID in the target channel")

            val messageEmbed = message.embeds.firstOrNull()
                ?: return@execute it.respond("This message does not contain an embed")

            val embed = messageEmbed.toEmbed(name)
            embedService.addEmbed(guild, embed)
            it.respond("Successfully copied the embed as: ${embed.name}")
        }
    }

    command("Import") {
        requiresGuild = true
        description = "Import a JSON String as an embed."
        expect(WordArg("Embed Name"), SentenceArg("JSON"))
        execute {
            val name = it.args.component1() as String
            val json = it.args.component2() as String
            val guild = it.guild!!

            if (guild.hasEmbedWithName(name))
                return@execute it.respond("An embed with this name already exists")

            it.respond(
                try {
                    val builder = gson.fromJson(json, EmbedBuilder::class.java)
                    val embed = Embed(name, builder)
                    val wasAdded = embedService.addEmbed(guild, embed)

                    if (wasAdded) "Successfully loaded the embed: ${embed.name}" else "An embed with this name already exists"
                } catch (e: Exception) {
                    "Invalid JSON!"
                }
            )
        }
    }

    command("Export") {
        requiresGuild = true
        description = "Export the currently loaded embed to JSON."
        execute {
            val embed = embedService.getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            it.respond(gson.toJson(embed.builder))
        }
    }
}