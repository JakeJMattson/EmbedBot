package io.github.jakejmattson.embedbot.extensions

import io.github.jakejmattson.embedbot.services.Permission
import me.aberrantfox.kjdautils.api.dsl.*
import java.util.WeakHashMap

fun CommandEvent.respondEmbed(init: EmbedDSLHandle.() -> Unit) {
    val embed = EmbedDSLHandle()
    embed.init()
    respond(embed.build())
}

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
    get() = CommandsContainerPropertyStore.permissions[this] ?: Permission.NONE
    set(value) { CommandsContainerPropertyStore.permissions[this] = value }

val Command.requiredPermissionLevel: Permission
    get() {
        val commandsContainerData = CommandsContainerPropertyStore.permissions
        val category = commandsContainerData.filter { it.key.commands.containsValue(this) }

        return category.toList().firstOrNull<Pair<CommandsContainer, Permission>>()?.second ?: Permission.NONE
    }