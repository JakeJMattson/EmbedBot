package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.*
import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.*

@CommandSet("Field")
fun fieldCommands() = commands {

    requiredPermissionLevel = Permission.STAFF

    command("AddField") {
        description = "Add a field in the following format: title|body|inline"
        requiresLoadedEmbed = true
        expect(FieldArg)
        execute {
            val field = it.args.component1() as Field
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.addField(field)
            it.respond("Field added.")
        }
    }

    command("AddBlankField") {
        description = "Add a blank field to the loaded embed."
        requiresLoadedEmbed = true
        expect(arg(BooleanArg("isInline"), optional = true, default = false))
        execute {
            val isInline = it.args.component1() as Boolean
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.addBlankField(isInline)
            it.respond("Field added.")
        }
    }

    command("InsertField") {
        description = "Insert a field at an index to the loaded embed."
        requiresLoadedEmbed = true
        expect(FieldIndexArg("Index"), FieldArg)
        execute {
            val index = it.args.component1() as Int
            val field = it.args.component2() as Field
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.insertField(index, field)
            it.respond("Field inserted at index $index")
        }
    }

    command("RemoveField") {
        description = "Remove a field from the loaded embed by its index."
        requiresLoadedEmbed = true
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
        requiresLoadedEmbed = true
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
        description = "Get a field by its index and edit its title value."
        requiresLoadedEmbed = true
        expect(FieldIndexArg, SentenceArg)
        execute {
            val index = it.args.component1() as Int
            val newTitle = it.args.component2() as String
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setFieldName(index, newTitle)
            it.respond("Field updated.")
        }
    }

    command("EditFieldText") {
        description = "Get a field by its index and edit its text value."
        requiresLoadedEmbed = true
        expect(FieldIndexArg, SentenceArg)
        execute {
            val index = it.args.component1() as Int
            val newText = it.args.component2() as String
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setFieldText(index, newText)
            it.respond("Field updated.")
        }
    }

    command("EditFieldInline") {
        description = "Get a field by its index and edit its inline value."
        requiresLoadedEmbed = true
        expect(FieldIndexArg, BooleanArg)
        execute {
            val index = it.args.component1() as Int
            val newInline = it.args.component2() as Boolean
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setFieldInline(index, newInline)
            it.respond("Field updated.")
        }
    }
}