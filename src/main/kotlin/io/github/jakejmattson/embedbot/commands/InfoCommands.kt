package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.EmbedArg
import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.locale.messages
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.command.*
import java.awt.Color

@CommandSet("Information")
fun infoCommands() = commands {
    command("Info") {
        description = messages.descriptions.INFO
        execute(EmbedArg.makeNullableOptional { it.guild!!.getLoadedEmbed() }) {
            val embed = it.args.first
                ?: return@execute it.respond(messages.errors.MISSING_OPTIONAL_EMBED)

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

                addField("Copied From", embed.copyLocationString)
            }
        }
    }

    command("ListEmbeds") {
        description = messages.descriptions.LIST_EMBEDS
        execute { event ->
            event.respond {
                val guild = event.guild!!
                val embeds = guild.getEmbeds()
                val clusters = guild.getClusters()
                val loadedEmbed = guild.getLoadedEmbed()
                val embedList = embeds.joinToString("\n") { it.name }.takeIf { it.isNotEmpty() } ?: "<No embeds>"

                color = Color(0x00bfff)

                if (loadedEmbed != null)
                    addField("Loaded", loadedEmbed.name)

                addField("Embeds", embedList)

                if (clusters.isNotEmpty()) {
                    clusters.forEach { cluster ->
                        addField(cluster.name, cluster.toString())
                    }
                }
            }
        }
    }

    command("Limits") {
        description = messages.descriptions.LIMITS
        execute {
            it.respond {
                title = "Discord Limits"
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
                addInlineField("", "[Discord Docs](${messages.links.DISCORD_EMBED_DOCS})")
            }
        }
    }
}