package io.github.jakejmattson.embedbot.extensions

import io.github.jakejmattson.embedbot.dataclasses.Embed
import io.github.jakejmattson.embedbot.services.*
import net.dv8tion.jda.core.entities.Guild

fun Guild.getLoadedEmbed() = getGuildEmbeds().loadedEmbed
fun Guild.hasEmbedWithName(name: String) = getGuildEmbeds().embedList.any { it.name.toLowerCase() == name.toLowerCase() }
fun Guild.loadEmbed(embed: Embed) = getGuildEmbeds().load(embed)
fun Guild.getEmbeds() = getGuildEmbeds().embedList.sortedBy { it.name }

fun Guild.hasClusterWithName(name: String) = getGuildClusters().any { it.name.toLowerCase() == name.toLowerCase() }
fun Guild.getClusterByName(name: String) = getGuildClusters().firstOrNull { it.name.toLowerCase() == name.toLowerCase() }
fun Guild.getClusters() = getGuildClusters().sortedBy { it.name }