package io.github.jakejmattson.embedbot.utilities

import me.aberrantfox.kjdautils.api.dsl.CommandsContainer

operator fun String.times(x: Int) = this.repeat(x)

fun generateDocs(commandsContainer: CommandsContainer): String {
    data class CommandData(val name: String, val args: String, val description: String) {
        fun format(format: String) = String.format(format, name, args, description)
    }
    val headers = CommandData("Commands", "Arguments", "Description")
    val docs = StringBuilder()

    commandsContainer.commands.values.groupBy { it.category }.toList().forEach {
        docs.appendln("## ${it.first}")

        val categoryCommands = it.second.map {
            CommandData(
                it.name,
                it.expectedArgs.joinToString { it.type.name }.takeIf { it.isNotEmpty() } ?: "<none>",
                it.description
            )
        } as ArrayList

        categoryCommands.add(headers)

        val longestName = categoryCommands.maxBy { it.name.length }!!.name.length
        val longestArgs = categoryCommands.maxBy { it.args.length }!!.args.length
        val longestDescription = categoryCommands.maxBy { it.description.length }!!.description.length
        val format = "| %-${longestName}s | %-${longestArgs}s | %-${longestDescription}s |"

        categoryCommands.remove(headers)

        docs.appendln(String.format(format, "Commands", "Arguments", "Description"))
        docs.appendln(String.format(format, "-" * longestName, "-" * longestArgs, "-" * longestDescription))

        categoryCommands.sortedBy { it.name }.forEach {
            docs.appendln(it.format(format))
        }

        docs.appendln()
    }

    return docs.toString()
}