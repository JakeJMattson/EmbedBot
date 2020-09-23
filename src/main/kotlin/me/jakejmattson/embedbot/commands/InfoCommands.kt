package me.jakejmattson.embedbot.commands

import me.jakejmattson.discordkt.api.annotations.CommandSet
import me.jakejmattson.discordkt.api.dsl.command.commands
import me.jakejmattson.embedbot.arguments.EmbedArg
import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.utils.*
import java.awt.Color

@CommandSet("Information")
fun infoCommands() = commands {
    command("Info") {
        description = "Get extended info for the target embed."
        execute(EmbedArg.makeNullableOptional { it.guild!!.getLoadedEmbed() }) {
            val embed = it.args.first
                ?: return@execute it.respond(messages.MISSING_OPTIONAL_EMBED)

            val guild = it.guild!!

            it.respond {
                color = Color(0x00bfff)
                addField("Embed Name", embed.name)
                addField("Is Loaded", embed.isLoaded(guild).toString())

                if (!embed.isEmpty) {
                    addField("Field Count", "${embed.fieldCount}/$FIELD_LIMIT")
                    addField("Character Count", "${embed.charCount}/$CHAR_LIMIT")
                } else {
                    addField("Is Empty", embed.isEmpty.toString())
                }

                addField("Copied From", embed.locationString)
            }
        }
    }

    command("ListEmbeds") {
        description = "List all embeds created in this guild."
        execute { event ->
            event.respond {
                val guild = event.guild!!
                val embeds = guild.getEmbeds()
                val groups = guild.getGroups()
                val loadedEmbed = guild.getLoadedEmbed()
                val embedList = embeds.joinToString("\n") { it.name }.takeIf { it.isNotEmpty() } ?: "<No embeds>"

                color = Color(0x00bfff)

                if (loadedEmbed != null)
                    addField("Loaded", loadedEmbed.name)

                addField("Embeds", embedList)

                if (groups.isNotEmpty()) {
                    groups.forEach { group ->
                        addField(group.name, group.toString())
                    }
                }
            }
        }
    }

    command("Limits") {
        description = "Display the discord embed limits."
        execute {
            it.respond {
                title {
                    text = "Discord Limits"
                }
                description = "Below are all the limits imposed onto embeds by Discord."
                color = Color(0x00bfff)

                addInlineField("Total Character Limit", "$CHAR_LIMIT characters")
                addInlineField("Total Field Limit", "$FIELD_LIMIT fields")
                addInlineField("Title", "$TITLE_LIMIT characters")
                addInlineField("Description", "$DESCRIPTION_LIMIT characters")
                addInlineField("Field Name", "$FIELD_NAME_LIMIT characters")
                addInlineField("Field Value", "$FIELD_VALUE_LIMIT characters")
                addInlineField("Footer", "$FOOTER_LIMIT characters")
                addInlineField("Author", "$AUTHOR_LIMIT characters")
                addInlineField("", "[Discord Docs](https://discordapp.com/developers/docs/resources/channel#embed-limits-limits)")
            }
        }
    }
}