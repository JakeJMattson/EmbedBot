package me.jakejmattson.embedbot.commands

import me.jakejmattson.embedbot.arguments.*
import me.jakejmattson.embedbot.extensions.*
import me.jakejmattson.embedbot.locale.messages
import me.jakejmattson.embedbot.services.*
import me.jakejmattson.kutils.api.annotations.CommandSet
import me.jakejmattson.kutils.api.arguments.*
import me.jakejmattson.kutils.api.dsl.command.commands

@CommandSet("Field")
fun fieldCommands() = commands {
    command("AddField") {
        description = messages.descriptions.ADD_FIELD
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
        description = messages.descriptions.ADD_BLANK_FIELD
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
        description = messages.descriptions.INSERT_FIELD
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
        description = messages.descriptions.REMOVE_FIELD
        requiresLoadedEmbed = true
        execute(FieldIndexArg) {
            val index = it.args.first
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.removeField(index)
            it.reactSuccess()
        }
    }

    command("SetField") {
        description = messages.descriptions.EDIT_FIELD
        requiresLoadedEmbed = true
        execute(FieldIndexArg, FieldArg) {
            val (index, field) = it.args
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setField(index, field)
            it.reactSuccess()
        }
    }

    command("SetFieldTitle") {
        description = messages.descriptions.EDIT_FIELD_TITLE
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
        description = messages.descriptions.EDIT_FIELD_TEXT
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
        description = messages.descriptions.EDIT_FIELD_INLINE
        requiresLoadedEmbed = true
        execute(FieldIndexArg, BooleanArg) {
            val (index, newInline) = it.args
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setFieldInline(index, newInline)
            it.reactSuccess()
        }
    }
}