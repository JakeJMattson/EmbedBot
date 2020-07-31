package me.jakejmattson.embedbot.extensions

import me.jakejmattson.discordkt.api.extensions.jda.containsURL
import net.dv8tion.jda.api.entities.Message

fun Message.getEmbed() =
    if (containsURL() || embeds.isEmpty())
        null
    else
        embeds.first()