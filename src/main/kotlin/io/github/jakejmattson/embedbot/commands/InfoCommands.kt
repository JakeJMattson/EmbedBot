package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.EmbedArg
import io.github.jakejmattson.embedbot.dataclasses.Embed
import io.github.jakejmattson.embedbot.extensions.*
import me.aberrantfox.kjdautils.api.dsl.*

@CommandSet("Info")
fun infoCommands() = commands {

    command("Info") {
        description = "Get extended info for the target embed."
        expect(EmbedArg)
        execute {
            val embed = it.args.component1() as Embed
            val guild = it.guild!!

            val info =
                embed {
                    addField("Embed Name", embed.name, false)
                    addField("Is Loaded", embed.isLoaded(guild).toString(), false)

                    if (!embed.isEmpty) {
                        addField("Field Count", embed.fieldCount.toString(), false)
                        addField("Character Count", embed.charCount.toString(), false)
                    }
                    else {
                        addField("Is Empty", embed.isEmpty.toString(), false)
                    }

                    addField("Copied From", embed.copyLocation?.toString() ?: "<Not copied>", false)
                }

            it.respond(info)
        }
    }

    command("ListEmbeds") {
        description = "List all embeds created in this guild."
        execute {
            it.respond(
                embed {
                    val guild = it.guild!!

                    val embeds = guild.getEmbeds()
                    val clusters = guild.getClusters()

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

    command("Limits") {
        description = "Display the discord embed limits."
        execute {
            val bot = it.jda.selfUser
            val footerUrl = bot.avatarUrl ?: bot.defaultAvatarUrl

            it.respond(
                embed {
                    setTitle("Discord Limits")
                    setDescription("Below are all the limits imposed onto embeds by Discord.")
                    addField("Total Character Limit", "6000 characters", true)
                    addField("Total Field Limit", "25 fields", true)
                    addField("Title", "256 characters", true)
                    addField("Description", "2048 characters", true)
                    addField("Field Name", "256 characters", true)
                    addField("Field Value", "1024 characters", true)
                    addField("Footer", "2048 characters", true)
                    addField("Author", "256 characters", true)
                    addBlankField(true)
                    setFooter("https://discordapp.com/developers/docs/resources/channel#embed-limits-limits", footerUrl)
                }
            )
        }
    }
}