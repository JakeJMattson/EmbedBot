package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.services.PermissionsService
import me.aberrantfox.kjdautils.api.dsl.command.*
import me.aberrantfox.kjdautils.internal.arguments.SentenceArg
import me.aberrantfox.kjdautils.internal.command.CommandStruct

@CommandSet("Advanced")
fun advancedCommands(permissionsService: PermissionsService) = commands {
    command("ExecuteAll") {
        description = "Execute a batch of commands in sequence."
        execute(SentenceArg("Commands")) { event ->
            val rawInvocations = event.args.first.split("\n").filter { it.isNotEmpty() }
            val unknownCommands = mutableListOf<String>()
            val missingPermissions = mutableListOf<String>()

            val commandMap = rawInvocations.mapNotNull {
                val split = it.split(" ")

                val commandName = split.first()
                val command = event.container[commandName]

                if (command == null) {
                    unknownCommands.add(commandName)
                    return@mapNotNull null
                }

                val hasPermission = permissionsService.hasClearance(event.message.member!!, command.requiredPermissionLevel)

                if (!hasPermission) {
                    missingPermissions.add(commandName)
                    return@mapNotNull null
                }

                val args = split.drop(1)

                command to args
            }

            if (unknownCommands.isNotEmpty() || missingPermissions.isNotEmpty()) {
                val unknownResponse = when (unknownCommands.size) {
                    0 -> ""
                    1 -> "Unknown Command: ${unknownCommands.first()}"
                    else -> "Unknown Commands: ${unknownCommands.joinToString()}"
                }

                val permissionResponse = when (missingPermissions.size) {
                    0 -> ""
                    else -> "Missing Permissions: ${missingPermissions.joinToString()}"
                }

                val response =
                    if (unknownResponse.isNotEmpty() && permissionResponse.isNotEmpty())
                        unknownResponse + "\n" + permissionResponse
                    else
                        unknownResponse + permissionResponse

                return@execute event.respond(response)
            }

            if (commandMap.isEmpty())
                return@execute event.respond("No commands to execute!")

            executeCommands(event, commandMap)
        }
    }
}

private fun executeCommands(event: CommandEvent<*>, commandMap: List<Pair<Command, List<String>>>) {
    commandMap.forEach { (command, args) ->
        val struct = CommandStruct(command.names.first(), args, !event.stealthInvocation)
        val context = DiscordContext(event.stealthInvocation, event.discord, event.message, guild = event.guild)
        val newEvent = CommandEvent<ArgumentContainer>(struct, event.container, context)

        val conversionResult = command.localInvoke(args, newEvent)

        when (conversionResult) {
            is Result.Success -> command.invoke(conversionResult.results, newEvent)
            is Result.Error -> return event.respond("Error in ${command.names}: ${conversionResult.error}")
        }
    }
}