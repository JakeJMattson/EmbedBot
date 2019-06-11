package io.github.jakejmattson.embedbot.extensions

import io.github.jakejmattson.embedbot.dataclasses.Embed
import io.github.jakejmattson.embedbot.services.getGuildEmbeds
import net.dv8tion.jda.core.entities.Guild

fun Guild.getLoadedEmbed() = getGuildEmbeds().loadedEmbed
fun Guild.hasEmbedWithName(name: String) = getGuildEmbeds().embedList.any { it.name.toLowerCase() == name.toLowerCase() }
fun Guild.loadEmbed(embed: Embed) = getGuildEmbeds().load(embed)
fun Guild.listEmbeds() = getGuildEmbeds().embedList.sortedBy { it.name }.joinToString("\n") { it.name }