package me.jakejmattson.embedbot.services

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.aberrantfox.kjdautils.api.annotation.Service
import me.jakejmattson.embedbot.dataclasses.*
import me.jakejmattson.embedbot.extensions.*
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.*
import java.io.File

typealias Field = MessageEmbed.Field

val gson = GsonBuilder().setPrettyPrinting().create()!!

private val embedFile = File("config/embeds.json")
private val embedMap = loadEmbeds()

fun Guild.getGuildEmbeds() = embedMap.getOrPut(this.id) { GuildEmbeds(null, arrayListOf(), arrayListOf()) }

@Service
class EmbedService {
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
        } else
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

fun saveEmbeds() = embedFile.writeText(gson.toJson(embedMap))

fun createEmbedFromJson(name: String, json: String) = Embed(name, gson.fromJson(json, EmbedBuilder::class.java))

private fun loadEmbeds() =
    if (embedFile.exists())
        gson.fromJson(embedFile.readText(), object : TypeToken<HashMap<String, GuildEmbeds>>() {}.type)
    else
        HashMap<String, GuildEmbeds>()