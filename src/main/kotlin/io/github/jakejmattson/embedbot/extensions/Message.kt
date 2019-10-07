package io.github.jakejmattson.embedbot.extensions

import me.aberrantfox.kjdautils.extensions.jda.containsURL
import net.dv8tion.jda.api.entities.Message

fun Message.getEmbed() =
    if (containsURL() || embeds.isEmpty())
        null
    else
        embeds.first()