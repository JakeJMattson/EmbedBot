package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.EmbedArg
import io.github.jakejmattson.embedbot.dataclasses.Embed
import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import java.awt.Color

@CommandSet("Information")
fun infoCommands() = commands {

    requiredPermissionLevel = Permission.STAFF

    command("Info") {
        description = "Get extended info for the target embed."
        expect(EmbedArg)
        execute {
            val embed = it.args.component1() as Embed
            val guild = it.guild!!

            it.respondEmbed {
                addField("Embed Name", embed.name)
                addField("Is Loaded", embed.isLoaded(guild).toString())

                if (!embed.isEmpty) {
                    addField("Field Count", embed.fieldCount.toString())
                    addField("Character Count", embed.charCount.toString())
                }
                else {
                    addField("Is Empty", embed.isEmpty.toString())
                }

                addField("Copied From", embed.copyLocationString)
            }
        }
    }

    command("ListEmbeds") {
        description = "List all embeds created in this guild."
        execute {
            it.respondEmbed {
                val guild = it.guild!!
                val embeds = guild.getEmbeds()
                val clusters = guild.getClusters()
                val loadedEmbed = guild.getLoadedEmbed()?.name ?: "<None>"
                val embedList = embeds.joinToString("\n") { it.name }.takeIf { it.isNotEmpty() }?: "<No embeds>"

                addField("Loaded", loadedEmbed)
                addField("Embeds", embedList)

                if (clusters.isEmpty())
                    addField("Clusters", "<No clusters>")
                else
                    clusters.forEach {
                        addField(it.name, it.toString())
                    }
            }
        }
    }

    command("Limits") {
        description = "Display the discord embed limits."
        execute {
            it.respondEmbed {
                title = "Discord Limits"
                description = "Below are all the limits imposed onto embeds by Discord."
                color = Color.orange
                addInlineField("Total Character Limit", "$CHAR_LIMIT characters")
                addInlineField("Total Field Limit", "$FIELD_LIMIT fields")
                addInlineField("Title", "$TITLE_LIMIT characters")
                addInlineField("Description", "$DESCRIPTION_LIMIT characters")
                addInlineField("Field Name", "$FIELD_NAME_LIMIT characters")
                addInlineField("Field Value", "$FIELD_VALUE_LIMIT characters")
                addInlineField("Footer", "$FOOTER_LIMIT characters")
                addInlineField("Author", "$AUTHOR_LIMIT characters")
                addInlineField("","[Discord Docs](https://discordapp.com/developers/docs/resources/channel#embed-limits-limits)")
            }
        }
    }
}