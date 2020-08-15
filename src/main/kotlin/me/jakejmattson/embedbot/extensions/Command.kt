package me.jakejmattson.embedbot.extensions

import me.jakejmattson.discordkt.api.dsl.command.*
import me.jakejmattson.embedbot.services.*
import java.util.*

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