package io.github.jakejmattson.embedbot.dataclasses

data class Cluster(var name: String, private val embeds: ArrayList<Embed> = arrayListOf()) {
    val size: Int
        get() = embeds.size

    fun getEmbedByName(name: String) = embeds.firstOrNull { it.name.toLowerCase() == name.toLowerCase() }
    fun addEmbed(embed: Embed) = embeds.add(embed)
    fun removeEmbed(embed: Embed) = embeds.remove(embed)
    fun build() = embeds.filter { !it.isEmpty }.map { it.build() }

    override fun toString() = embeds.joinToString { it.name }.takeIf { it.isNotEmpty() }?: "<No embeds>"
}