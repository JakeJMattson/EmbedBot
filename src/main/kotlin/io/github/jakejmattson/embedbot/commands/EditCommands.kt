package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.*
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.*
import java.awt.Color

@CommandSet
fun editCommands(embedService: EmbedService) = commands {
    command("SetTitle") {
        requiresGuild = true
        description = "Set the embed title."
        expect(EmbedArg, SentenceArg)
        execute {
            val embed = it.args.component1() as Embed
            val title = it.args.component2() as String

            embed.setTitle(title)

            it.respond("Successfully updated the embed title!")
        }
    }

    command("SetDescription") {
        requiresGuild = true
        description = "Set the embed description."
        expect(EmbedArg, SentenceArg)
        execute {
            val embed = it.args.component1() as Embed
            val description = it.args.component2() as String

            embed.setDescription(description)

            it.respond("Successfully updated the embed description!")
        }
    }

    command("SetAuthor") {
        requiresGuild = true
        description = "Set the embed author."
        expect(EmbedArg, SentenceArg)
        execute {
            val embed = it.args.component1() as Embed
            val description = it.args.component2() as String

            embed.setAuthor(description)

            it.respond("Successfully updated the embed author!")
        }
    }

    command("SetColor") {
        requiresGuild = true
        description = "Set the embed color."
        expect(EmbedArg, HexColorArg)
        execute {
            val embed = it.args.component1() as Embed
            val color = it.args.component2() as Int

            embed.setColor(color)

            it.respond("Successfully updated the embed color!")
        }
    }
}