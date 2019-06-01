package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.*
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*

@CommandSet("Field")
fun fieldCommands() = commands {
    command("AddField") {
        requiresGuild = true
        description = "Add a field in the following format: title|body|inline"
        expect(FieldArg)
        execute {
            val field = it.args.component1() as Field

            val embed = getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            embed.addField(field)
        }
    }

    command("RemoveField") {
        requiresGuild = true
        description = "Remove a field from the loaded embed by its index."
        expect(FieldIndexArg)
        execute {
            val index = it.args.component1() as Int

            val embed = getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            embed.removeField(index)
        }
    }

    command("EditField") {
        requiresGuild = true
        description = "Edit a field at a given index with the given data."
        expect(FieldIndexArg, FieldArg)
        execute {
            val index = it.args.component1() as Int
            val field = it.args.component2() as Field

            val embed = getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            embed.setField(index, field)
        }
    }
}