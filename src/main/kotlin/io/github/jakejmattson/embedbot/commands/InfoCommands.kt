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
                    addField("Embed Name", embed.name)
                    addField("Is Loaded", embed.isLoaded(guild).toString())

                    if (!embed.isEmpty) {
                        addField("Field Count", embed.fieldCount.toString())
                        addField("Character Count", embed.charCount.toString())
                    }
                    else {
                        addField("Is Empty", embed.isEmpty.toString())
                    }

                    addField("Copied From", embed.copyLocation?.toString() ?: "<Not copied>")
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
                    addField("Loaded", loadedEmbed)

                    val embedList = embeds.joinToString("\n") { it.name }.takeIf { it.isNotEmpty() }?: "<No embeds>"
                    addField("Embeds", embedList)

                    if (clusters.isEmpty())
                        addField("Clusters", "<No clusters>")
                    else
                        clusters.forEach {
                            addField(it.name, it.toString())
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
                    addInlineField("Total Character Limit", "6000 characters")
                    addInlineField("Total Field Limit", "25 fields")
                    addInlineField("Title", "256 characters")
                    addInlineField("Description", "2048 characters")
                    addInlineField("Field Name", "256 characters")
                    addInlineField("Field Value", "1024 characters")
                    addInlineField("Footer", "2048 characters")
                    addInlineField("Author", "256 characters")
                    addBlankField(true)
                    setFooter("https://discordapp.com/developers/docs/resources/channel#embed-limits-limits", footerUrl)
                }
            )
        }
    }
}