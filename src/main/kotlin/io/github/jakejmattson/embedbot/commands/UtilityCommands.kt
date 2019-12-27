package io.github.jakejmattson.embedbot.commands

import io.github.jakejmattson.embedbot.dataclasses.getFileSystemLocation
import io.github.jakejmattson.embedbot.discordToken
import io.github.jakejmattson.embedbot.locale.messages
import io.github.jakejmattson.embedbot.services.GitHubService
import me.aberrantfox.kjdautils.api.dsl.command.*
import me.aberrantfox.kjdautils.extensions.stdlib.toMinimalTimeString
import java.awt.Color
import java.util.Date
import kotlin.system.exitProcess

private val startTime = Date()

@CommandSet("Utility")
fun utilityCommands(gitHubService: GitHubService) = commands {
    command("Status", "Ping", "Uptime") {
        description = messages.descriptions.STATUS
        execute { event ->
            val jda = event.discord.jda

            jda.restPing.queue { restPing ->
                event.respond {
                    color = Color(0x00bfff)

                    val seconds = (Date().time - startTime.time) / 1000

                    addField("Rest ping", "${restPing}ms")
                    addField("Gateway ping", "${jda.gatewayPing}ms")
                    addField("Total Uptime", seconds.toMinimalTimeString())
                }
            }
        }
    }

    command("Restart") {
        description = "Restart the bot via the JAR file."
        execute {
            val currentJar = getFileSystemLocation()

            if (currentJar.extension != ".jar")
                return@execute it.respond("Could not restart. The bot needs to be running from a JAR.")

            it.respond("Restarting...")
            startJar(currentJar.path)
        }
    }

    command("Update") {
        description = "Update the bot to the latest version."
        execute {
            it.respond("Update in progress...")

            val updateResponse = gitHubService.update()

            if (!updateResponse.wasSuccessful)
                return@execute it.respond(updateResponse.message)

            it.respond("Download complete. Proceeding with update.")
            startJar(updateResponse.message)
        }
    }
}

private fun startJar(path: String) {
    val command = "java -jar $path $discordToken"
    Runtime.getRuntime().exec(command)
    exitProcess(0)
}