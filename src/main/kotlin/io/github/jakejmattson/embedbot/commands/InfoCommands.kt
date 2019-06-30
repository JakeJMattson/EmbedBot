package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.EmbedArg
import io.github.jakejmattson.embedbot.dataclasses.Embed
import io.github.jakejmattson.embedbot.extensions.*
import me.aberrantfox.kjdautils.api.dsl.*

@CommandSet("Info")
fun infoCommands() = commands {

    command("Info") {
        requiresGuild = true
        description = "Get extended info for the target embed."
        expect(EmbedArg)
        execute {
            val embed = it.args.component1() as Embed
            val guild = it.guild!!

            val info =
                embed {
                    addField("Embed Name", embed.name, false)
                    addField("Is Empty", embed.isEmpty.toString(), false)
                    addField("Is Loaded", embed.isLoaded(guild).toString(), false)
                    addField("Field Count", embed.fieldCount.toString(), false)

                    addField("Copied From",
                        if (embed.copyLocation == null)
                            "<Not copied>"
                        else
                            "Channel ID: ${embed.copyLocation.channelId}\n" +
                                "Message ID: ${embed.copyLocation.messageId}",
                        false
                    )
                }

            it.respond(info)
        }
    }

    command("ListEmbeds") {
        requiresGuild = true
        description = "List all embeds created in this guild."
        execute {
            it.respond(
                embed {
                    val guild = it.guild!!

                    val embeds = it.guild!!.getEmbeds()
                    val clusters = it.guild!!.getClusters()

                    val loadedEmbed = guild.getLoadedEmbed()?.name ?: "<None>"
                    addField("Loaded", loadedEmbed, false)

                    val embedList = embeds.joinToString("\n") { it.name }.takeIf { it.isNotEmpty() }?: "<No embeds>"
                    addField("Embeds", embedList, false)

                    if (clusters.isEmpty())
                        addField("Clusters", "<No clusters>", false)
                    else
                        clusters.forEach {
                            addField(it.name, it.toString(), false)
                        }
                }
            )
        }
    }
}