package io.github.jakejmattson.embedbot.services

import com.google.gson.reflect.TypeToken
import io.github.jakejmattson.embedbot.dataclasses.*
import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.utilities.*
import me.aberrantfox.kjdautils.api.annotation.Service
import net.dv8tion.jda.core.entities.*
import java.io.File

typealias Field = MessageEmbed.Field

private lateinit var embedMap: HashMap<String, GuildEmbeds>
private val embedFile = File("config/embeds.json")

fun Guild.getGuildEmbeds() = embedMap.getOrPut(this.id) { GuildEmbeds(null, arrayListOf(), arrayListOf()) }

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

    fun removeAllFromGuild(guild: Guild): Int {
        val embeds = guild.getGuildEmbeds()
        val removed = embeds.clear()
        embedMap.remove(guild.id)
        saveEmbeds()
        return removed
    }

    fun createCluster(guild: Guild, name: String) =
        if (!guild.hasClusterWithName(name)) {
            val clusters = guild.getGuildEmbeds().clusterList
            val newCluster = Cluster(name)

            clusters.add(newCluster)
            saveEmbeds()
            newCluster
        }
        else
            null

    fun createClusterFromEmbeds(guild: Guild, cluster: Cluster): Boolean {
        val clusters = guild.getGuildEmbeds().clusterList

        if (guild.hasClusterWithName(cluster.name))
            return false

        clusters.add(cluster)
        saveEmbeds()
        return true
    }

    fun deleteCluster(guild: Guild, cluster: Cluster): Boolean {
        val clusters = guild.getGuildEmbeds().clusterList
        clusters.remove(cluster)
        saveEmbeds()
        return true
    }

    fun removeEmbedFromCluster(guild: Guild, embed: Embed) {
        guild.removeEmbed(embed)
        addEmbed(guild, embed)
        saveEmbeds()
    }
}

fun saveEmbeds() = save(embedFile, embedMap)

private fun loadEmbeds() =
    if (embedFile.exists())
        gson.fromJson(embedFile.readText(), object : TypeToken<HashMap<String, GuildEmbeds>>() {}.type)
    else
        HashMap<String, GuildEmbeds>()