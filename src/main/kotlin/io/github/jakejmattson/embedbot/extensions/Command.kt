package io.github.jakejmattson.embedbot.extensions

import me.aberrantfox.kjdautils.api.dsl.*
import java.util.WeakHashMap

fun CommandEvent.respondEmbed(init: EmbedDSLHandle.() -> Unit) {
    val embed = EmbedDSLHandle()
    embed.init()
    respond(embed.build())
}

private object PropertyStore {
    val severities = WeakHashMap<Command, Boolean>()
}

var Command.requiresLoadedEmbed
    get() = PropertyStore.severities[this] ?: false
    set(value) {
        PropertyStore.severities[this] = value
    }