package me.jakejmattson.embedbot.dataclasses

data class GuildEmbeds(var loadedEmbed: Embed? = null, val embedList: MutableList<Embed> = mutableListOf(), val groupList: MutableList<Group> = mutableListOf()) {
    val size: Int
        get() = embedList.size + groupList.sumBy { it.size }

    fun addAndLoad(embed: Embed) {
        embedList.add(embed)
        load(embed)
    }

    fun load(embed: Embed) {
        loadedEmbed = embed
    }

    fun clear(): Int {
        val removed = embedList.size + groupList.sumBy { it.size }

        loadedEmbed = null
        embedList.clear()
        groupList.clear()

        return removed
    }
}