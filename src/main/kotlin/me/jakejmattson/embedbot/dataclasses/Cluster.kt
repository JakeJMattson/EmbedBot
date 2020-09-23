package me.jakejmattson.embedbot.dataclasses

import me.jakejmattson.embedbot.extensions.removeEmbed
import me.jakejmattson.embedbot.services.saveEmbeds
import net.dv8tion.jda.api.entities.Guild

data class Group(var name: String, val embeds: MutableList<Embed> = mutableListOf()) {
    val size: Int
        get() = embeds.size

    fun getEmbedByName(name: String) = embeds.firstOrNull { it.name.toLowerCase() == name.toLowerCase() }
    fun removeEmbed(embed: Embed) = embeds.remove(embed)

    fun addEmbed(guild: Guild, embed: Embed, index: Int = -1) {
        guild.removeEmbed(embed)

        if (index == -1)
            embeds.add(embed)
        else
            embeds.add(index, embed)

        saveEmbeds()
    }

    override fun toString() = embeds.joinToString { it.name }.takeIf { it.isNotEmpty() } ?: "<No embeds>"
}