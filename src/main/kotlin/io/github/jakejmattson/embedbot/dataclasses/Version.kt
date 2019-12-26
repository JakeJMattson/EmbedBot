package io.github.jakejmattson.embedbot.dataclasses

data class Version(val major: Int = 0, val minor: Int = 0, val patch: Int = 0) {
    operator fun compareTo(other: Version): Int {
        return when {
            major > other.major -> 1
            other.major > major -> -1
            minor > other.minor -> 1
            other.minor > minor -> -1
            patch > other.patch -> 1
            other.patch > patch -> -1
            else -> 0
        }
    }

    override fun toString() = "$major.$minor.$patch"
}

fun String.parseToVersion(): Version {
    val split = split(".").mapNotNull { it.toIntOrNull() }

    return when (split.size) {
        0 -> Version()
        1 -> Version(split[0])
        2 -> Version(split[0], split[1])
        else -> Version(split[0], split[1], split[2])
    }
}