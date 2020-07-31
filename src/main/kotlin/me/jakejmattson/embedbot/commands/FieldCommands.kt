package me.jakejmattson.embedbot.commands

import me.jakejmattson.discordkt.api.annotations.CommandSet
import me.jakejmattson.discordkt.api.arguments.*
import me.jakejmattson.discordkt.api.dsl.command.commands
import me.jakejmattson.embedbot.arguments.*
import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.utils.*

@CommandSet("Field")
fun fieldCommands() = commands {
    command("AddField") {
        description = "Add a field in the following format: title|body|inline"
        requiresLoadedEmbed = true
        execute(FieldArg) {
            val field = it.args.first
            val embed = it.guild!!.getLoadedEmbed()!!

            if (embed.fieldCount == FIELD_LIMIT)
                return@execute it.respond("Embeds can only hold $FIELD_LIMIT fields.")

            embed.addField(field)
            it.reactSuccess()
        }
    }

    command("AddBlankField") {
        description = "Add a blank field to the loaded embed."
        requiresLoadedEmbed = true
        execute(BooleanArg("isInline").makeOptional(false)) {
            val isInline = it.args.first
            val embed = it.guild!!.getLoadedEmbed()!!

            if (embed.fieldCount == FIELD_LIMIT)
                return@execute it.respond("Embeds can only hold $FIELD_LIMIT fields.")

            embed.addBlankField(isInline)
            it.reactSuccess()
        }
    }

    command("InsertField") {
        description = "Insert a field at an index to the loaded embed."
        requiresLoadedEmbed = true
        execute(FieldIndexArg("Index"), FieldArg) {
            val (index, field) = it.args
            val embed = it.guild!!.getLoadedEmbed()!!

            if (embed.fieldCount == FIELD_LIMIT)
                return@execute it.respond("Embeds can only hold $FIELD_LIMIT fields.")

            embed.insertField(index, field)
            it.reactSuccess()
        }
    }

    command("RemoveField") {
        description = "Remove a field from the loaded embed by its index."
        requiresLoadedEmbed = true
        execute(FieldIndexArg) {
            val index = it.args.first
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.removeField(index)
            it.reactSuccess()
        }
    }

    command("SetField") {
        description = "Edit a field at a given index with the given data."
        requiresLoadedEmbed = true
        execute(FieldIndexArg, FieldArg) {
            val (index, field) = it.args
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setField(index, field)
            it.reactSuccess()
        }
    }

    command("SetFieldTitle") {
        description = "Get a field by its index and edit its title value."
        requiresLoadedEmbed = true
        execute(FieldIndexArg, EveryArg) {
            val (index, newTitle) = it.args
            val embed = it.guild!!.getLoadedEmbed()!!

            if (newTitle.length > FIELD_NAME_LIMIT)
                return@execute it.respond("Max field name length is $FIELD_NAME_LIMIT characters. Input was ${newTitle.length}.")

            embed.setFieldName(index, newTitle)
            it.reactSuccess()
        }
    }

    command("SetFieldText") {
        description = "Get a field by its index and edit its text value."
        requiresLoadedEmbed = true
        execute(FieldIndexArg, EveryArg) {
            val (index, newText) = it.args
            val embed = it.guild!!.getLoadedEmbed()!!

            if (newText.length > FIELD_VALUE_LIMIT)
                return@execute it.respond("Max field value length is $FIELD_VALUE_LIMIT characters. Input was ${newText.length}.")

            embed.setFieldText(index, newText)
            it.reactSuccess()
        }
    }

    command("SetFieldInline") {
        description = "Get a field by its index and edit its inline value."
        requiresLoadedEmbed = true
        execute(FieldIndexArg, BooleanArg) {
            val (index, newInline) = it.args
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setFieldInline(index, newInline)
            it.reactSuccess()
        }
    }
}