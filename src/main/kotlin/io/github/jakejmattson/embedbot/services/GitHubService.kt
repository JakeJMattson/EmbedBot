package io.github.jakejmattson.embedbot.services

import io.github.jakejmattson.embedbot.dataclasses.*
import me.aberrantfox.kjdautils.api.annotation.Service
import org.kohsuke.github.GitHubBuilder
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

private const val repoId = "177512683"

@Service
class GitHubService(private val configuration: Configuration) {
    fun update(): OperationResult {
        val repo = try {
            val github = GitHubBuilder().withOAuthToken(configuration.githubToken).build()
            github.getRepositoryById(repoId)
        }
        catch (e: Exception) {
            return false withMessage "Failed to connect to repo. Likely missing github access token in the config file."
        }

        val release = repo.latestRelease

        val jar = release.assets.firstOrNull { it.name.endsWith("jar") } ?:
            return false withMessage "Failed to locate JAR in release assets."

        println("Download: ${jar.browserDownloadUrl}")

        downloadJar(jar.browserDownloadUrl, jar.name)

        return true withMessage "Updating to ${release.tagName}"
    }

    private fun downloadJar(url: String, name: String) {
        val readChannel = Channels.newChannel(URL(url).openStream())
        val currentLocation = getFileSystemLocation()
        val fileOS = FileOutputStream("${currentLocation.parent}/$name")
        val writeChannel = fileOS.channel

        writeChannel.transferFrom(readChannel, 0, Long.MAX_VALUE)
    }
}