package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.extensions.*
import me.aberrantfox.kjdautils.api.dsl.command.*
import me.aberrantfox.kjdautils.internal.arguments.SentenceArg
import me.aberrantfox.kjdautils.internal.command.CommandStruct

@CommandSet("Advanced")
fun advancedCommands() = commands {
    command("ExecuteAll") {
        description = "Execute a batch of commands in sequence."
        execute(SentenceArg("Commands")) { event ->
            val commandString = event.args.first
            val container = event.container
            val blocks = commandString.split("\n")

            val commandMap = blocks.mapNotNull {
                val split = it.split(" ")

                if (it.isEmpty())
                    return@mapNotNull null

                val commandName = split.first()
                val command = container[commandName]
                    ?: return@execute event.respond("Unknown command: $commandName")

                val args = split.drop(1)

                command to args
            }

            commandMap.forEach { (command, args) ->
                println("Command: ${command.names} - Args: $args")

                val conversionResult = command.localInvoke(args, event)

                val struct = CommandStruct(command.names.first(), args, !event.stealthInvocation)
                val context = DiscordContext(event.stealthInvocation, event.discord, event.message, guild = event.guild)
                val newEvent = CommandEvent<ArgumentContainer>(struct, container, context)

                when (conversionResult) {
                    is Result.Success -> command.invoke(conversionResult.results, newEvent)
                    is Result.Error -> return@forEach event.respond("Error in ${command.names}: ${conversionResult.error}")
                }
            }
        }
    }
}