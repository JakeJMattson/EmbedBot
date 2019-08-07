package io.github.jakejmattson.embedbot.utilities

import com.google.gson.GsonBuilder
import io.github.jakejmattson.embedbot.dataclasses.Embed
import net.dv8tion.jda.api.EmbedBuilder
import java.io.File

val gson = GsonBuilder().setPrettyPrinting().create()!!

fun createEmbedFromJson(name: String, json: String) = Embed(name, gson.fromJson(json, EmbedBuilder::class.java))

fun save(file: File, data: Any) = file.writeText(gson.toJson(data))