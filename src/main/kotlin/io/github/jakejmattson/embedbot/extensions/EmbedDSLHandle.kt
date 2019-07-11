package io.github.jakejmattson.embedbot.extensions

import me.aberrantfox.kjdautils.api.dsl.EmbedDSLHandle
import java.awt.Color

fun EmbedDSLHandle.addField(name: String, value: String) = addField(name, value, false)!!
fun EmbedDSLHandle.addInlineField(name: String, value: String) = addField(name, value, true)!!

var EmbedDSLHandle.title: String
    set(value) { this.title(value) }
    get() = ""

var EmbedDSLHandle.description: String
    set(value) { this.description(value) }
    get() = ""

var EmbedDSLHandle.thumbnail: String
    set(value) { this.setThumbnail(value) }
    get() = ""

var EmbedDSLHandle.color: Color
    set(value) { this.color(value) }
    get() = Color.black