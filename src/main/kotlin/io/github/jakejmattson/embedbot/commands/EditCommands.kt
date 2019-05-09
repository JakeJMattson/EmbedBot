package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.EmbedArg
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.*

@CommandSet
fun editCommands(embedService: EmbedService) = commands {
    command("SetTitle") {
        requiresGuild = true
        expect(EmbedArg, SentenceArg)
        execute {
            val embed = it.args.component1() as Embed
            val title = it.args.component2() as String

            embedService.setTitle(embed, title)

            it.respond("Successfully updated the embed title!")
        }
    }
}