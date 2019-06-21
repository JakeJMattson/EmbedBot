package io.github.jakejmattson.embedbot.extensions

import io.github.jakejmattson.embedbot.dataclasses.Embed
import io.github.jakejmattson.embedbot.services.*
import net.dv8tion.jda.core.entities.Guild

fun Guild.getEmbeds() = getGuildEmbeds().embedList.sortedBy { it.name }
fun Guild.getLoadedEmbed() = getGuildEmbeds().loadedEmbed
fun Guild.getEmbedByName(name: String): Embed? {

    val embed = getGuildEmbeds().embedList.firstOrNull { it.name.toLowerCase() == name.toLowerCase() }

    if (embed != null)
        return embed

    getGuildClusters().forEach {
        val embedInCluster = it.embeds.firstOrNull { it.name.toLowerCase() == name.toLowerCase() }

        if (embedInCluster != null)
            return embedInCluster
    }

    return null
}
fun Guild.hasEmbedWithName(name: String) = getEmbedByName(name) != null

fun Guild.loadEmbed(embed: Embed) = getGuildEmbeds().load(embed)

fun Guild.getClusters() = getGuildClusters().sortedBy { it.name }
fun Guild.getClusterByName(name: String) = getGuildClusters().firstOrNull { it.name.toLowerCase() == name.toLowerCase() }
fun Guild.hasClusterWithName(name: String) = getClusterByName(name) != null