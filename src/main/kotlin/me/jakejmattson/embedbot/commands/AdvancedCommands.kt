package me.jakejmattson.embedbot.commands

import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.*
import me.aberrantfox.kjdautils.internal.arguments.*
import me.aberrantfox.kjdautils.internal.command.*
import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.services.PermissionsService

@CommandSet("Advanced")
fun advancedCommands(permissionsService: PermissionsService) = commands {
    command("ExecuteAll") {
        description = "Execute a batch of commands in sequence."
        execute(EveryArg("Commands")) { event ->
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

                command to split.drop(1)
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
        val struct = RawInputs("", command.names.first(), args, event.rawInputs.prefixCount)
        val context = DiscordContext(event.discord, event.message)
        val newEvent = CommandEvent<GenericContainer>(struct, event.container, context)

        val conversionResult = command.localInvoke(args, newEvent)

        when (conversionResult) {
            is Result.Success -> TODO("Method requires GenericContainer")//command.invoke(conversionResult.results, newEvent)
            is Result.Error -> event.respond("Error in ${command.names}: ${conversionResult.error}")
        }
    }
}