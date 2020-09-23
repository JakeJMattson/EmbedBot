package me.jakejmattson.embedbot.services

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.jakejmattson.discordkt.api.annotations.Service
import me.jakejmattson.embedbot.dataclasses.*
import me.jakejmattson.embedbot.extensions.*
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.*
import java.io.File

typealias Field = MessageEmbed.Field

val gson = GsonBuilder().setPrettyPrinting().create()!!

private val embedFile = File("config/embeds.json")
private val embedMap = loadEmbeds()

fun Guild.getGuildEmbeds() = embedMap.getOrPut(this.id) { GuildEmbeds() }

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

    fun createGroup(guild: Guild, name: String) =
        if (!guild.hasGroupWithName(name)) {
            val groups = guild.getGuildEmbeds().groupList
            val newGroup = Group(name)

            groups.add(newGroup)
            saveEmbeds()
            newGroup
        } else
            null

    fun createGroupFromEmbeds(guild: Guild, group: Group): Boolean {
        val groups = guild.getGuildEmbeds().groupList

        if (guild.hasGroupWithName(group.name))
            return false

        groups.add(group)
        saveEmbeds()
        return true
    }

    fun deleteGroup(guild: Guild, group: Group): Boolean {
        val groups = guild.getGuildEmbeds().groupList
        groups.remove(group)
        saveEmbeds()
        return true
    }

    fun removeEmbedFromGroup(guild: Guild, embed: Embed) {
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