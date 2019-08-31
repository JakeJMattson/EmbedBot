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
        expect(FieldArg)
        execute {
            val field = it.args.component1() as Field
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
        expect(arg(BooleanArg("isInline"), optional = true, default = false))
        execute {
            val isInline = it.args.component1() as Boolean
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
        expect(FieldIndexArg("Index"), FieldArg)
        execute {
            val index = it.args.component1() as Int
            val field = it.args.component2() as Field
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
        expect(FieldIndexArg)
        execute {
            val index = it.args.component1() as Int
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.removeField(index)
            it.reactSuccess()
        }
    }

    command("EditField") {
        description = messages.descriptions.EDIT_FIELD
        requiresLoadedEmbed = true
        expect(FieldIndexArg, FieldArg)
        execute {
            val index = it.args.component1() as Int
            val field = it.args.component2() as Field
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setField(index, field)
            it.reactSuccess()
        }
    }

    command("EditFieldTitle") {
        description = messages.descriptions.EDIT_FIELD_TITLE
        requiresLoadedEmbed = true
        expect(FieldIndexArg, SentenceArg)
        execute {
            val index = it.args.component1() as Int
            val newTitle = it.args.component2() as String
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
        expect(FieldIndexArg, SentenceArg)
        execute {
            val index = it.args.component1() as Int
            val newText = it.args.component2() as String
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
        expect(FieldIndexArg, BooleanArg)
        execute {
            val index = it.args.component1() as Int
            val newInline = it.args.component2() as Boolean
            val embed = it.guild!!.getLoadedEmbed()!!

            embed.setFieldInline(index, newInline)
            it.reactSuccess()
        }
    }
}