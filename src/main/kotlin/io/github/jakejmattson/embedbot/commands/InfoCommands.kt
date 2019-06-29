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
                with(embed) {
                    embed {
                        addField("Embed Name", name, false)
                        addField("Is Empty", isEmpty.toString(), false)
                        addField("Is Loaded", isLoaded(guild).toString(), false)
                        addField("Field Count", (lastFieldIndex + 1).toString(), false)

                        addField("Copied From",
                            if (copyLocation == null)
                                "<Not copied>"
                            else
                                "Channel ID: ${copyLocation.channelId}\n" +
                                    "Message ID: ${copyLocation.messageId}",
                            false
                        )
                    }
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
                    val embeds = it.guild!!.getEmbeds()
                    val clusters = it.guild!!.getClusters()

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