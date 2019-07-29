package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.*
import io.github.jakejmattson.embedbot.extensions.getLoadedEmbed
import io.github.jakejmattson.embedbot.services.Field
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.SentenceArg
import net.dv8tion.jda.core.entities.MessageEmbed

@CommandSet("Field")
fun fieldCommands() = commands {
    command("AddField") {
        description = "Add a field in the following format: title|body|inline"
        expect(FieldArg)
        execute {
            val field = it.args.component1() as Field
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.addField(field)
            it.respond("Field added.")
        }
    }

    command("RemoveField") {
        description = "Remove a field from the loaded embed by its index."
        expect(FieldIndexArg)
        execute {
            val index = it.args.component1() as Int
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.removeField(index)
            it.respond("Field removed.")
        }
    }

    command("EditField") {
        description = "Edit a field at a given index with the given data."
        expect(FieldIndexArg, FieldArg)
        execute {
            val index = it.args.component1() as Int
            val field = it.args.component2() as Field
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setField(index, field)
            it.respond("Field updated.")
        }
    }

    command("EditFieldTitle") {
        description = "Edit a field's title at the given index."
        expect(FieldIndexArg, SentenceArg)
        execute {
            val index = it.args.component1() as Int
            val newTitle = it.args.component2() as String
            val embed = it.guild!!.getLoadedEmbed()!!

            val oldField = embed.fields[index]
            val newField = MessageEmbed.Field(newTitle, oldField.value, oldField.isInline)

            embed.setField(index, newField)
            it.respond("Field updated.")
        }
    }

    command("EditFieldText") {
        description = "Edit a field's text at the given index."
        expect(FieldIndexArg, SentenceArg)
        execute {
            val index = it.args.component1() as Int
            val newText = it.args.component2() as String
            val embed = it.guild!!.getLoadedEmbed()!!

            val oldField = embed.fields[index]
            val newField = MessageEmbed.Field(oldField.name, newText, oldField.isInline)

            embed.setField(index, newField)
            it.respond("Field updated.")
        }
    }

    command("EditFieldInline") {
        description = "Edit a field's inline at the given index."
        expect(FieldIndexArg, BooleanArg)
        execute {
            val index = it.args.component1() as Int
            val newInline = it.args.component2() as Boolean
            val embed = it.guild!!.getLoadedEmbed()!!

            val oldField = embed.fields[index]
            val newField = MessageEmbed.Field(oldField.name, oldField.value, newInline)

            embed.setField(index, newField)
            it.respond("Field updated.")
        }
    }
}