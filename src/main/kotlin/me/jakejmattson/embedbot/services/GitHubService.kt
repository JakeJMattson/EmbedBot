package me.jakejmattson.embedbot.services

import me.aberrantfox.kjdautils.api.annotation.Service
import me.jakejmattson.embedbot.Properties
import me.jakejmattson.embedbot.commands.getFileSystemLocation
import me.jakejmattson.embedbot.dataclasses.*
import org.kohsuke.github.GitHubBuilder
import java.io.*
import java.net.URL
import java.nio.channels.Channels

private const val repoId = "177512683"

@Service
class GitHubService(private val configuration: Configuration, private val project: Properties) {
    fun update(): OperationResult {
        val repo = try {
            val github = GitHubBuilder().withOAuthToken(configuration.githubToken).build()
            github.getRepositoryById(repoId)
        } catch (e: Exception) {
            return false withMessage "Failed to connect to repo. Likely missing github access token in the config file."
        }

        val release = repo.latestRelease

        val jar = release.assets.firstOrNull { it.name.endsWith("jar") }
            ?: return false withMessage "Failed to locate JAR in release assets."

        val newVersion = release.tagName.parseToVersion()
        val currentVersion = project.version.parseToVersion()

        if (newVersion <= currentVersion)
            return false withMessage "No updates available.\n```" +
                "Current: $currentVersion\n" +
                "Release: $newVersion```"

        val currentLocation = getFileSystemLocation()
        val filePath = "${currentLocation.parent}${File.separator}${jar.name}"

        try {
            downloadJar(jar.browserDownloadUrl, filePath)
        } catch (e: Exception) {
            return false withMessage "Something went wrong while downloading the update. Aborting.\n```${e.message}```"
        }

        return true withMessage filePath
    }

    @Throws(Exception::class)
    private fun downloadJar(url: String, filePath: String) {
        val readChannel = Channels.newChannel(URL(url).openStream())
        val fileStream = FileOutputStream(filePath)

        fileStream.channel.transferFrom(readChannel, 0, Long.MAX_VALUE)
    }
}