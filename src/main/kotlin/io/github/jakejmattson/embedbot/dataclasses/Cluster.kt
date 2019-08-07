package io.github.jakejmattson.embedbot.dataclasses

import io.github.jakejmattson.embedbot.extensions.removeEmbed
import io.github.jakejmattson.embedbot.services.saveEmbeds
import net.dv8tion.jda.api.entities.Guild

data class Cluster(var name: String, val embeds: ArrayList<Embed> = arrayListOf()) {
    val size: Int
        get() = embeds.size

    fun getEmbedByName(name: String) = embeds.firstOrNull { it.name.toLowerCase() == name.toLowerCase() }
    fun removeEmbed(embed: Embed) = embeds.remove(embed)
    fun build() = embeds.filter { !it.isEmpty }.map { it.build() }

    override fun toString() = embeds.joinToString { it.name }.takeIf { it.isNotEmpty() }?: "<No embeds>"

    fun addEmbed(guild: Guild, embed: Embed) {
        guild.removeEmbed(embed)
        embeds.add(embed)
        saveEmbeds()
    }

    fun insertEmbed(guild: Guild, index: Int, embed: Embed) {
        guild.removeEmbed(embed)
        embeds.add(index, embed)
        saveEmbeds()
    }
}