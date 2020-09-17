package me.jakejmattson.embedbot.dataclasses

data class GuildEmbeds(var loadedEmbed: Embed?, val embedList: ArrayList<Embed>, val clusterList: ArrayList<Cluster>) {
    val size: Int
        get() = embedList.size + clusterList.sumBy { it.size }

    fun addAndLoad(embed: Embed) {
        embedList.add(embed)
        load(embed)
    }

    fun load(embed: Embed) {
        loadedEmbed = embed
    }

    fun clear(): Int {
        val removed = embedList.size + clusterList.sumBy { it.size }

        loadedEmbed = null
        embedList.clear()
        clusterList.clear()

        return removed
    }
}