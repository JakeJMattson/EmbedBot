package io.github.jakejmattson.embedbot.extensions

import me.aberrantfox.kjdautils.api.dsl.EmbedDSLHandle
import java.awt.Color
import java.util.WeakHashMap

fun EmbedDSLHandle.addField(name: String, value: String) = addField(name, value, false)
fun EmbedDSLHandle.addInlineField(name: String, value: String) = addField(name, value, true)

private object EmbedPropertyStore {
    val titles = WeakHashMap<EmbedDSLHandle, String>()
    val descriptions = WeakHashMap<EmbedDSLHandle, String>()
    val thumbnails = WeakHashMap<EmbedDSLHandle, String>()
    val colors = WeakHashMap<EmbedDSLHandle, Color>()
}

var EmbedDSLHandle.title
    get() = EmbedPropertyStore.titles[this] ?: ""
    set(value) {
        EmbedPropertyStore.titles[this] = value
        setTitle(value)
    }

var EmbedDSLHandle.description: String
    get() = EmbedPropertyStore.descriptions[this] ?: ""
    set(value) {
        EmbedPropertyStore.descriptions[this] = value
        setDescription(value)
    }

var EmbedDSLHandle.thumbnail: String
    get() = EmbedPropertyStore.thumbnails[this] ?: ""
    set(value) {
        EmbedPropertyStore.thumbnails[this] = value
        setThumbnail(value)
    }

var EmbedDSLHandle.color: Color
    get() = EmbedPropertyStore.colors[this] ?: Color.BLACK
    set(value) {
        EmbedPropertyStore.colors[this] = value
        setColor(value)
    }