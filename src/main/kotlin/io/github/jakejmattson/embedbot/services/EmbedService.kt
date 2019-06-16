package io.github.jakejmattson.embedbot.services

import com.google.gson.reflect.TypeToken
import io.github.jakejmattson.embedbot.dataclasses.Embed
import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.utilities.*
import me.aberrantfox.kjdautils.api.annotation.Service
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.*
import java.io.File

typealias Field = MessageEmbed.Field

private lateinit var embedMap: HashMap<String, GuildEmbeds>
private val clusterMap = HashMap<String, ArrayList<GuildCluster>>()

private val embedFile = File("config/embeds.json")

data class GuildEmbeds(var loadedEmbed: Embed?, val embedList: ArrayList<Embed>) {
    fun addAndLoad(embed: Embed) {
        embedList.add(embed)
        load(embed)
    }

    fun load(embed: Embed) {
        loadedEmbed = embed
    }
}

data class GuildCluster(var name: String, val embeds: ArrayList<EmbedBuilder>)

fun Guild.getGuildEmbeds() = embedMap.getOrPut(this.id) { GuildEmbeds(null, arrayListOf()) }
fun Guild.getGuildClusters() = clusterMap.getOrPut(this.id) { arrayListOf() }

@Service
class EmbedService {
    init {
        embedMap = loadEmbeds()
    }

    fun createEmbed(guild: Guild, name: String): Boolean {
        val embeds = guild.getGuildEmbeds()

        if (guild.hasEmbedWithName(name))
            return false

        val newEmbed = Embed(name)
        embeds.addAndLoad(newEmbed)
        saveEmbeds()
        return true
    }

    fun addEmbed(guild: Guild, embed: Embed): Boolean {
        val embeds = guild.getGuildEmbeds()

        if (embed in embeds.embedList)
            return false

        embeds.addAndLoad(embed)
        saveEmbeds()
        return true
    }

    fun removeEmbed(guild: Guild, embed: Embed): Boolean {
        val embeds = guild.getGuildEmbeds()

        if (embed !in embeds.embedList)
            return false

        if (embed.isLoaded(guild))
            embeds.loadedEmbed = null

        embeds.embedList.remove(embed)
        saveEmbeds()
        return true
    }

    fun removeAllFromGuild(guild: Guild): Int {
        val embeds = guild.getGuildEmbeds()
        val removed = embeds.embedList.size

        embeds.loadedEmbed = null
        embeds.embedList.toTypedArray().forEach {
            removeEmbed(guild, it)
        }

        return removed
    }

    fun createCluster(guild: Guild, cluster: GuildCluster): Boolean {
        val clusters = guild.getGuildClusters()

        if (guild.hasClusterWithName(cluster.name))
            return false

        clusters.add(cluster)
        return true
    }
}

private fun saveEmbeds() = save(embedFile, embedMap)

private fun loadEmbeds() =
    if (embedFile.exists()) {
        val type = object : TypeToken<HashMap<String, GuildEmbeds>>() {}.type
        gson.fromJson(embedFile.readText(), type)
    } else
        HashMap<String, GuildEmbeds>()


