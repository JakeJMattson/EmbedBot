package io.github.jakejmattson.embedbot.dataclasses

data class GuildEmbeds(var loadedEmbed: Embed?, val embedList: ArrayList<Embed>, val clusterList: ArrayList<Cluster>) {
    fun addAndLoad(embed: Embed) {
        embedList.add(embed)
        load(embed)
    }

    fun load(embed: Embed) {
        loadedEmbed = embed
    }
}