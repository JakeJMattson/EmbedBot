package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.*
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.*

@CommandSet("Edit")
fun editCommands(embedService: EmbedService) = commands {
    command("SetTitle") {
        requiresGuild = true
        description = "Set the title for the currently loaded embed."
        expect(SentenceArg)
        execute {
            val title = it.args.component1() as String

            val embed = embedService.getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            embed.setTitle(title)

            it.respond("Successfully updated the embed title!")
        }
    }

    command("SetDescription") {
        requiresGuild = true
        description = "Set the description for the currently loaded embed."
        expect(SentenceArg)
        execute {
            val description = it.args.component1() as String

            val embed = embedService.getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            embed.setDescription(description)

            it.respond("Successfully updated the embed description!")
        }
    }

    command("SetAuthor") {
        requiresGuild = true
        description = "Set the author for the currently loaded embed."
        expect(SentenceArg)
        execute {
            val description = it.args.component1() as String

            val embed = embedService.getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            embed.setAuthor(description)

            it.respond("Successfully updated the embed author!")
        }
    }

    command("SetColor") {
        requiresGuild = true
        description = "Set the color for the currently loaded embed."
        expect(HexColorArg)
        execute {
            val color = it.args.component1() as Int

            val embed = embedService.getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            embed.setColor(color)

            it.respond("Successfully updated the embed color!")
        }
    }

    command("SetThumbnail") {
        requiresGuild = true
        description = "Set the thumbnail for the currently loaded embed."
        expect(UrlArg)
        execute {
            val url = it.args.component1() as String

            val embed = embedService.getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            embed.setThumbnail(url)

            it.respond("Successfully updated the embed thumbnail!")
        }
    }
}