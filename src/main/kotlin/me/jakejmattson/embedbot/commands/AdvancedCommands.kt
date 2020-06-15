package me.jakejmattson.embedbot.commands

import me.jakejmattson.embedbot.extensions.requiredPermissionLevel
import me.jakejmattson.embedbot.services.PermissionsService
import me.jakejmattson.kutils.api.annotations.CommandSet
import me.jakejmattson.kutils.api.arguments.EveryArg
import me.jakejmattson.kutils.api.dsl.command.commands

@CommandSet("Advanced")
fun advancedCommands(permissionsService: PermissionsService) = commands {
    command("ExecuteAll") {
        description = "Execute a batch of commands in sequence."
        execute(EveryArg("Commands")) { event ->
            event.respond("Command currently disabled")
            if (true) return@execute

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
        }
    }
}