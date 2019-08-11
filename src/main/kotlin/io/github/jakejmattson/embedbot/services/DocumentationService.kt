package io.github.jakejmattson.embedbot.services

import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.api.dsl.*
import java.io.File
import java.util.Timer
import kotlin.concurrent.schedule

private val saveFile = File("commands.md")

@Service
class DocumentationService(private val container: CommandsContainer) {
    init {
        //Move the help command from the internal "utility" category, to the local "Utility" category
        container.commands.getValue("help").category = "Utility"
        
        Timer().schedule(500) {
            generateDocumentation()
        }
    }

    fun generateDocumentation() {
        val sortOrder = arrayListOf("BotConfiguration", "GuildConfiguration", "Core", "Copy", "Field",
            "Cluster", "Edit", "Information", "Utility")
        val categories = container.commands.values.groupBy { it.category }
        val categoryDocs = generateDocsByCategory(categories)

        val sortedDocs =
            if (sortOrder.isNotEmpty()) {
                sortCategoryDocs(categoryDocs, sortOrder)
            } else {
                categoryDocs.sortedBy { it.name }
            }

        outputDocs(sortedDocs)
    }

    private fun generateDocsByCategory(categories: Map<String, List<Command>>) =
        categories.map { generateSingleCategoryDoc(it) } as ArrayList<CategoryDocs>

    private fun sortCategoryDocs(categoryDocs: ArrayList<CategoryDocs>, categoryNameOrder: List<String>): List<CategoryDocs> {
        val sortedCategories = categoryDocs
            .filter { cat -> categoryNameOrder.any { it.toLowerCase() == cat.name.toLowerCase() } }
            .sortedBy { cat -> categoryNameOrder.indexOfFirst { it == cat.name } }.toMutableList()

        //add back anything that was missing
        sortedCategories.addAll(categoryDocs.filter { !sortedCategories.contains(it) })

        return sortedCategories.toList()
    }

    private fun outputDocs(rawDocs: List<CategoryDocs>) {
        val indentLevel = "##"
        val docsAsString =
            "# Commands\n\n" +
                "$indentLevel Key\n" +
                "| Symbol     | Meaning                    |\n" +
                "| ---------- | -------------------------- |\n" +
                "| (Argument) | This argument is optional. |\n\n" +
                buildString {
                    rawDocs.forEach {
                        appendln("$indentLevel ${it.name}\n${it.docString}")
                    }
                }

        saveFile.writeText(docsAsString)
    }

    private fun generateSingleCategoryDoc(entry: Map.Entry<String, List<Command>>): CategoryDocs {
        val commandData = entry.value.map { it.toCommandData() }
        val commandDataFormat = generateFormat(commandData)
        val separator = generateSeparator(commandDataFormat)

        val commandString = commandData
            .sortedBy { it.name }
            .joinToString("\n"){ it.format(commandDataFormat.generateFormatString()) }

        val docs =
            """;;-${HEADER_DATA.format(commandDataFormat.generateFormatString())}
               ;;-$separator
               ;;-$commandString
               ;;-
            """.trimMargin(";;-")

        return CategoryDocs(entry.key, docs)
    }

    private fun generateSeparator(cformat: CommandsOutputFormatter) = with(cformat) {
        kotlin.String.format(cformat.generateFormatString(), "-".repeat(longestName), "-".repeat(longestArgs), "-".repeat(longestDescription))
    }

    private fun generateFormat(commandData: List<CommandData>): CommandsOutputFormatter {
        val longestName = commandData.maxBy { it.name.length }!!.name.length
        val longestArgs = commandData.maxBy { it.args.length }!!.args.length
        val longestDescription = commandData.maxBy { it.description.length }!!.description.length

        return CommandsOutputFormatter().apply {
            //check to see if any of the real data was longer than our pre-defined default values
            this.longestArgs = maxOf(this.longestArgs, longestArgs)
            this.longestName = maxOf(this.longestName, longestName)
            this.longestDescription = maxOf(this.longestDescription, longestDescription)
        }
    }


}

private val HEADER_DATA = CommandData("Commands", "Arguments", "Description")

private data class CategoryDocs(val name: String, val docString: String)

private data class CommandData(val name: String, val args: String, val description: String) {
    fun format(format: String) = String.format(format, name, args, description)
}

private data class CommandsOutputFormatter(
    var longestName: Int = HEADER_DATA.name.length,
    var longestArgs: Int = HEADER_DATA.args.length,
    var longestDescription: Int = HEADER_DATA.description.length) {
    fun generateFormatString() = "| %-${longestName}s | %-${longestArgs}s | %-${longestDescription}s |"
}

private fun Command.toCommandData(): CommandData {
    val expectedArgs = expectedArgs.joinToString {
        if (it.optional) "(${it.type.name})" else it.type.name
    }.takeIf { it.isNotEmpty() } ?: "<none>"

    return CommandData(name, expectedArgs, description.replace("|", "\\|"))
}