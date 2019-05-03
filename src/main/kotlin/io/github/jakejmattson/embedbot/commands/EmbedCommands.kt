package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.services.EmbedService
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.WordArg

@CommandSet
fun embedCommands(embedService: EmbedService) = commands {
    command("SendEmbed") {
        requiresGuild = true
        expect(WordArg)
        execute {
            val embedName = it.args.component1() as String

            val embed = embedService.getEmbed(it.guild!!, embedName)

            embed ?: return@execute it.respond("No such embed")
            if (embed.isEmpty) return@execute

            it.respond(embed.build())
        }
    }

    command("AddEmbed") {
        requiresGuild = true
        expect(WordArg)
        execute {
            val embedName = it.args.component1() as String

            embedService.addEmbed(it.guild!!, embedName)
        }
    }

    command("RemoveEmbed") {
        requiresGuild = true
        expect(WordArg)
        execute {
            val embedName = it.args.component1() as String

            embedService.removeEmbed(it.guild!!, embedName)
        }
    }

    command("ListEmbeds") {
        requiresGuild = true
        execute {
            it.respond(embedService.listEmbeds(it.guild!!))
        }
    }
}