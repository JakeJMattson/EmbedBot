package me.jakejmattson.embedbot.extensions

import me.aberrantfox.kjdautils.api.dsl.command.*
import me.aberrantfox.kjdautils.internal.command.*
import me.jakejmattson.embedbot.services.*
import java.util.WeakHashMap

fun CommandEvent<*>.reactSuccess() = message.addReaction("✅").queue()

private object CommandPropertyStore {
    val requiresLoaded = WeakHashMap<Command, Boolean>()
    val permissions = WeakHashMap<Command, Permission>()
}

var Command.requiresLoadedEmbed
    get() = CommandPropertyStore.requiresLoaded[this] ?: false
    set(value) {
        CommandPropertyStore.requiresLoaded[this] = value
    }

var Command.requiredPermissionLevel: Permission
    get() = CommandPropertyStore.permissions[this] ?: DEFAULT_REQUIRED_PERMISSION
    set(value) {
        CommandPropertyStore.permissions[this] = value
    }

fun Command.localInvoke(args: List<String>, commandEvent: CommandEvent<*>) = convertArguments(args, arguments, commandEvent)

private fun convertOptional(arg: ArgumentType<*>, event: CommandEvent<*>) = arg.defaultValue?.invoke(event)

fun convertArguments(actual: List<String>, expected: List<ArgumentType<*>>, event: CommandEvent<*>): Result {
    val remainingArgs = actual.toMutableList().filter { it.isNotBlank() }.toMutableList()

    val converted = expected.map { expectedArg ->
        if (remainingArgs.isEmpty()) {
            if (expectedArg.isOptional)
                convertOptional(expectedArg, event)
            else {
                val conversion = expectedArg.convert("", emptyList(), event)
                    .takeIf { it is ArgumentResult.Success && it.consumed == 0 }
                    ?: return Result.Error("Received less arguments than expected.")

                (conversion as ArgumentResult.Success).result
            }
        }
        else {
            val firstArg = remainingArgs.first()
            val result = expectedArg.convert(firstArg, remainingArgs, event)

            when (result) {
                is ArgumentResult.Success -> {
                    if (result.consumed > remainingArgs.size) {
                        if (!expectedArg.isOptional)
                            return Result.Error("Received less arguments than expected.")
                    }
                    else
                        remainingArgs.subList(0, result.consumed).toList().forEach { remainingArgs.remove(it) }

                    result.result
                }
                is ArgumentResult.Error -> {
                    if (expectedArg.isOptional)
                        convertOptional(expectedArg, event)
                    else
                        return Result.Error(result.error)
                }
            }
        }
    }

    if (remainingArgs.isNotEmpty())
        return Result.Error("Received more arguments than expected.")

    return Result.Success(converted as List<Any>)
}