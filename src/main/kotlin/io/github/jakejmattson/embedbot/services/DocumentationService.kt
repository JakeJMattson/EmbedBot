package io.github.jakejmattson.embedbot.services

import io.github.jakejmattson.embedbot.dataclasses.Configuration
import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.api.dsl.CommandsContainer
import java.util.Timer
import kotlin.concurrent.schedule

@Service
class DocumentationService(configuration: Configuration, container: CommandsContainer) {

    data class CategoryDocs(val name: String, val docString: String, private val indentLevel: String = "##") {
        override fun toString() = "$indentLevel $name\n$docString"
    }

    init {
        container.commands.getValue("help").category = "Utility"

        if (configuration.generateDocsAtRuntime)
            Timer().schedule(500) {
                val sortOrder = arrayListOf("BotConfiguration", "GuildConfiguration", "Core", "Copy",
                    "Field", "Cluster", "Edit", "Info", "Utility")

                val categoryDocs = generateCategoryDocs(container)
                val sortedDocs = sortCategoryDocs(categoryDocs, sortOrder)

                sortedDocs.forEach {
                    println(it.toString())
                }
            }
    }

    private fun generateCategoryDocs(commandsContainer: CommandsContainer): ArrayList<CategoryDocs> {

        data class CommandData(val name: String, val args: String, val description: String) {
            fun format(format: String) = String.format(format, name, args, description)
        }

        val commandDocs = arrayListOf<CategoryDocs>()

        commandsContainer.commands.values.groupBy { it.category }.toList().forEach {
            val categoryCommands = it.second.map {
                CommandData(
                    it.name,
                    it.expectedArgs.joinToString {
                        if (it.optional)
                            "(${it.type.name})"
                        else
                            it.type.name
                    }.takeIf { it.isNotEmpty() } ?: "<none>",
                    it.description.replace("|", "\\|")
                )
            } as ArrayList

            with(categoryCommands) {
                operator fun String.times(x: Int) = this.repeat(x)

                val headers = CommandData("Commands", "Arguments", "Description")
                add(headers)

                val longestName = maxBy { it.name.length }!!.name.length
                val longestArgs = maxBy { it.args.length }!!.args.length
                val longestDescription = maxBy { it.description.length }!!.description.length
                val format = "| %-${longestName}s | %-${longestArgs}s | %-${longestDescription}s |"

                remove(headers)

                val docs = StringBuilder()
                docs.appendln(headers.format(format))
                docs.appendln(String.format(format, "-" * longestName, "-" * longestArgs, "-" * longestDescription))

                sortedBy { it.name }.forEach {
                    docs.appendln(it.format(format))
                }

                commandDocs.add(CategoryDocs(it.first, docs.toString()))
            }
        }

        return commandDocs
    }

    private fun sortCategoryDocs(categoryDocs: ArrayList<CategoryDocs>, sortOrder: ArrayList<String>): ArrayList<CategoryDocs> {
        val sortedDocs = arrayListOf<CategoryDocs>()

        sortOrder.forEach { sortString ->
            val category = categoryDocs.firstOrNull { sortString == it.name }

            if (category != null)
                sortedDocs.add(category)
        }

        return sortedDocs
    }
}