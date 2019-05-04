package io.github.jakejmattson.embedbot.services

import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.api.dsl.embed
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.*

data class Embed(val name: String, val builder: EmbedBuilder = EmbedBuilder())

private val embedMap = HashMap<String, ArrayList<Embed>>()

fun getGuildEmbeds(guildId: String) : ArrayList<Embed> {
    if (!embedMap.containsKey(guildId))
        embedMap[guildId] = arrayListOf()

    return embedMap[guildId]!!
}

@Service
class EmbedService {
    fun addEmbed(guild: Guild, name: String) {
        getGuildEmbeds(guild.id).add(Embed(name))
    }

    fun removeEmbed(guild: Guild, embed: Embed) {
        getGuildEmbeds(guild.id).remove(embed)
    }

    fun listEmbeds(guild: Guild) = getGuildEmbeds(guild.id).joinToString("\n") { it.name }
}