package io.github.jakejmattson.embedbot

import io.github.jakejmattson.embedbot.locale.messages
import me.aberrantfox.kjdautils.api.startBot

fun main(args: Array<String>) {
    val token = args.firstOrNull()
            ?: throw IllegalArgumentException(messages.errors.NO_ARGS)

    startBot(token) {
        configure {
            globalPath = "io.github.jakejmattson.embedbot"
            reactToCommands = false
            documentationSortOrder = arrayListOf("BotConfiguration", "GuildConfiguration", "Core", "Copy", "Field",
                "Cluster", "Edit", "Information", "Utility")

            //Move the help command from the internal "utility" category, to the local "Utility" category
            container.commands.getValue("help").category = "Utility"
        }
    }
}