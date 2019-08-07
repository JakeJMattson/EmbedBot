package io.github.jakejmattson.embedbot.extensions

import io.github.jakejmattson.embedbot.dataclasses.Embed
import io.github.jakejmattson.embedbot.services.getGuildEmbeds
import net.dv8tion.jda.api.entities.Guild

fun Guild.getEmbeds() = getGuildEmbeds().embedList.sortedBy { it.name }
fun Guild.getClusters() = getGuildEmbeds().clusterList.sortedBy { it.name }

fun Guild.hasLoadedEmbed() = getLoadedEmbed() != null
fun Guild.getLoadedEmbed() = getGuildEmbeds().loadedEmbed
fun Guild.loadEmbed(embed: Embed) = getGuildEmbeds().load(embed)

fun Guild.getEmbedByName(name: String): Embed? {
    val embed = getEmbeds().firstOrNull { it.name.toLowerCase() == name.toLowerCase() }

    if (embed != null)
        return embed

    getClusters().forEach {
        val embedInCluster = it.getEmbedByName(name)

        if (embedInCluster != null)
            return embedInCluster
    }

    return null
}
fun Guild.hasEmbedWithName(name: String) = getEmbedByName(name) != null

fun Guild.getClusterByName(name: String) = getClusters().firstOrNull { it.name.toLowerCase() == name.toLowerCase() }
fun Guild.hasClusterWithName(name: String) = getClusterByName(name) != null

fun Guild.removeEmbed(embed: Embed) {
    if (embed.isLoaded(this))
        getGuildEmbeds().loadedEmbed = null

    getGuildEmbeds().embedList.remove(embed)
    getGuildEmbeds().clusterList.forEach {
        it.removeEmbed(embed)
    }
}