package io.github.jakejmattson.embedbot.extensions

import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.EmbedDSLHandle
import me.aberrantfox.kjdautils.api.dsl.command.*
import java.awt.Color
import java.util.WeakHashMap

fun CommandEvent<*>.reactSuccess() = message.addReaction("✅").queue()

private object CommandPropertyStore {
    val requiresLoaded = WeakHashMap<Command, Boolean>()
}

var Command.requiresLoadedEmbed
    get() = CommandPropertyStore.requiresLoaded[this] ?: false
    set(value) { CommandPropertyStore.requiresLoaded[this] = value }

private object CommandsContainerPropertyStore {
    val permissions = WeakHashMap<CommandsContainer, Permission>()
}

var CommandsContainer.requiredPermissionLevel
    get() = CommandsContainerPropertyStore.permissions[this] ?: DEFAULT_REQUIRED_PERMISSION
    set(value) { CommandsContainerPropertyStore.permissions[this] = value }

val Command.requiredPermissionLevel: Permission
    get() = CommandsContainerPropertyStore.permissions.toList()
        .firstOrNull { it.first.commands.containsValue(this) }?.second ?: DEFAULT_REQUIRED_PERMISSION