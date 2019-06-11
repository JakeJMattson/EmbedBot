package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.data.Configuration
import io.github.jakejmattson.embedbot.services.PrefixService
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.WordArg
import me.aberrantfox.kjdautils.internal.di.PersistenceService

@CommandSet("BotConfiguration")
fun botConfigurationCommands(configuration: Configuration, prefixService: PrefixService, persistenceService: PersistenceService) = commands {
    command("SetPrefix") {
        requiresGuild = true
        description = "Set the prefix required for the bot to register a command."
        expect(WordArg("Prefix"))
        execute {
            val prefix = it.args.component1() as String

            prefixService.setPrefix(prefix)
            persistenceService.save(configuration)

            it.respond("Prefix set to: $prefix")
        }
    }
}