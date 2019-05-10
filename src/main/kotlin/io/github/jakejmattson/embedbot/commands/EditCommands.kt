package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.EmbedArg
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.*

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
}