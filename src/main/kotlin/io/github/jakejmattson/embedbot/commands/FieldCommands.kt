package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.services.EmbedService
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.*
import net.dv8tion.jda.core.entities.MessageEmbed

@CommandSet("Field")
fun fieldCommands(embedService: EmbedService) = commands {
    command("AddField") {
        requiresGuild = true
        description = "Add a field in the following format: title|body|inline"
        expect(SplitterArg("Field"))
        execute {
            val fieldData = it.args.component1() as List<String>

            val embed = embedService.getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            if (fieldData.size !in 2..3)
                return@execute it.respond("Invalid number of arguments.")

            val field = MessageEmbed.Field(
                fieldData.component1(),
                fieldData.component2(),
                if (fieldData.size == 3) fieldData.component3().toBoolean() else false
            )

            embed.addField(field)
        }
    }

    command("RemoveField") {
        requiresGuild = true
        description = "Remove a field from the loaded embed by its index."
        expect(IntegerArg("Field Index"))
        execute {
            val index = it.args.component1() as Int

            val embed = embedService.getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            if (index !in 0 until embed.builder.length())
                return@execute it.respond("Invalid index.")

            embed.removeField(index)
        }
    }

    command("EditField") {
        requiresGuild = true
        description = "Edit a field at a given index with the given data."
        expect(IntegerArg, SplitterArg("Field"))
        execute {
            val index = it.args.component1() as Int
            val fieldData = it.args.component2() as List<String>

            val embed = embedService.getLoadedEmbed(it.guild!!)
                ?: return@execute it.respond("No embed loaded!")

            if (index !in 0 until embed.builder.length())
                return@execute it.respond("Invalid index.")

            if (fieldData.size !in 2..3)
                return@execute it.respond("Invalid number of arguments.")

            val field = MessageEmbed.Field(
                fieldData.component1(),
                fieldData.component2(),
                if (fieldData.size == 3) fieldData.component3().toBoolean() else false
            )

            embed.setField(index, field)
        }
    }
}