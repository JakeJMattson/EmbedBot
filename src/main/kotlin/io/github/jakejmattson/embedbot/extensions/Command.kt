package io.github.jakejmattson.embedbot.extensions

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