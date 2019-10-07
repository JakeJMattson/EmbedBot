package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.arguments.*
import io.github.jakejmattson.embedbot.extensions.*
import io.github.jakejmattson.embedbot.locale.messages
import io.github.jakejmattson.embedbot.services.*
import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.arguments.*

@CommandSet("Field")
fun fieldCommands() = commands {
    command("AddField") {
        description = messages.descriptions.ADD_FIELD
        requiresLoadedEmbed = true
        execute(FieldArg) {
            val field = it.args.component1()
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
            val isInline = it.args.component1()
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
            val index = it.args.component1()
            val field = it.args.component2()
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
            val index = it.args.component1()
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.removeField(index)
            it.reactSuccess()
        }
    }

    command("EditField") {
        description = messages.descriptions.EDIT_FIELD
        requiresLoadedEmbed = true
        execute(FieldIndexArg, FieldArg) {
            val index = it.args.component1()
            val field = it.args.component2()
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setField(index, field)
            it.reactSuccess()
        }
    }

    command("EditFieldTitle") {
        description = messages.descriptions.EDIT_FIELD_TITLE
        requiresLoadedEmbed = true
        execute(FieldIndexArg, SentenceArg) {
            val index = it.args.component1()
            val newTitle = it.args.component2()
            val embed = it.guild!!.getLoadedEmbed()!!

            if (newTitle.length > FIELD_NAME_LIMIT)
                return@execute it.respond("Max field name length is $FIELD_NAME_LIMIT characters. Input was ${newTitle.length}.")

            embed.setFieldName(index, newTitle)
            it.reactSuccess()
        }
    }

    command("EditFieldText") {
        description = messages.descriptions.EDIT_FIELD_TEXT
        requiresLoadedEmbed = true
        execute(FieldIndexArg, SentenceArg) {
            val index = it.args.component1()
            val newText = it.args.component2()
            val embed = it.guild!!.getLoadedEmbed()!!

            if (newText.length > FIELD_VALUE_LIMIT)
                return@execute it.respond("Max field value length is $FIELD_VALUE_LIMIT characters. Input was ${newText.length}.")

            embed.setFieldText(index, newText)
            it.reactSuccess()
        }
    }

    command("EditFieldInline") {
        description = messages.descriptions.EDIT_FIELD_INLINE
        requiresLoadedEmbed = true
        execute(FieldIndexArg, BooleanArg) {
            val index = it.args.component1()
            val newInline = it.args.component2()
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setFieldInline(index, newInline)
            it.reactSuccess()
        }
    }
}