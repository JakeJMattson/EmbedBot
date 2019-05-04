package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.EmbedArg
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.WordArg

@CommandSet
fun embedCommands(embedService: EmbedService) = commands {
    command("SendEmbed") {
        requiresGuild = true
        expect(EmbedArg)
        execute {
            val embed = (it.args.component1() as Embed).builder

            if (embed.isEmpty) return@execute it.respond("This embed is empty.")

            it.respond(embed.build())
        }
    }

    command("AddEmbed") {
        requiresGuild = true
        expect(WordArg)
        execute {
            val embedName = it.args.component1() as String

            embedService.addEmbed(it.guild!!, embedName)

            it.respond("Successfully added the embed: $embedName")
        }
    }

    command("RemoveEmbed") {
        requiresGuild = true
        expect(EmbedArg)
        execute {
            val embed = it.args.component1() as Embed

            embedService.removeEmbed(it.guild!!, embed)

            it.respond("Successfully removed the embed: ${embed.name}")
        }
    }

    command("ListEmbeds") {
        requiresGuild = true
        execute {
            it.respond(embedService.listEmbeds(it.guild!!))
        }
    }
}