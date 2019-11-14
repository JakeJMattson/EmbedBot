package io.github.jakejmattson.embedbot.extensions

import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.command.*
import me.aberrantfox.kjdautils.internal.command.ArgumentResult
import java.util.WeakHashMap

sealed class Result {
    data class Success(val results: ArgumentContainer) : Result()
    data class Error(val error: String) : Result()
}

fun CommandEvent<*>.reactSuccess() = message.addReaction("✅").queue()

private object CommandPropertyStore {
    val requiresLoaded = WeakHashMap<Command, Boolean>()
}

var Command.requiresLoadedEmbed
    get() = CommandPropertyStore.requiresLoaded[this] ?: false
    set(value) {
        CommandPropertyStore.requiresLoaded[this] = value
    }

private object CommandsContainerPropertyStore {
    val permissions = WeakHashMap<CommandsContainer, Permission>()
}

var CommandsContainer.requiredPermissionLevel
    get() = CommandsContainerPropertyStore.permissions[this] ?: DEFAULT_REQUIRED_PERMISSION
    set(value) {
        CommandsContainerPropertyStore.permissions[this] = value
    }

val Command.requiredPermissionLevel: Permission
    get() = CommandsContainerPropertyStore.permissions.toList()
        .firstOrNull { this in it.first.commands }?.second ?: DEFAULT_REQUIRED_PERMISSION

fun Command.localInvoke(args: List<String>, commandEvent: CommandEvent<*>) = convertArguments(args, expectedArgs, commandEvent)

private fun convertArguments(args: List<String>, expected: ArgumentCollection<*>, event: CommandEvent<*>): Result {
    val remaining = args.toMutableList()
    val currentArg = remaining.firstOrNull() ?: ""

    val convertedArgs = expected.arguments.map { expectedArg ->
        if (expectedArg.isOptional) {
            val default = expectedArg.defaultValue?.invoke(event)

            if (remaining.isEmpty())
                return@map default

            val convertedOptional = expectedArg.convert(currentArg, remaining, event)

            when (convertedOptional) {
                is ArgumentResult.Success -> {
                    val consumed = convertedOptional.consumed.takeIf { it.isNotEmpty() } ?: listOf(currentArg)
                    remaining.removeAll(consumed)
                    convertedOptional.result
                }
                is ArgumentResult.Error -> default
            }
        } else {
            if (remaining.isEmpty())
                return Result.Error("Missing argument")

            val result = expectedArg.convert(currentArg, remaining, event)

            when (result) {
                is ArgumentResult.Success -> {
                    val consumed = result.consumed.takeIf { it.isNotEmpty() } ?: listOf(currentArg)
                    remaining.removeAll(consumed)
                    result.result
                }
                is ArgumentResult.Error -> return Result.Error(result.error)
            }
        }
    }

    if (remaining.isNotEmpty())
        Result.Error("Unmatched Input: " + remaining.joinToString(" "))

    return Result.Success(expected.bundle(convertedArgs as List<Any>))
}