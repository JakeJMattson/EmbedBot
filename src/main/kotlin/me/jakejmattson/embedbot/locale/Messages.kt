package me.jakejmattson.embedbot.locale

val messages = Messages()

class Messages(
    val MISSING_GUILD: String = "Must be invoked inside a guild.",
    val EMBED_ALREADY_EXISTS: String = "An embed with this name already exists.",
    val CLUSTER_ALREADY_EXISTS: String = "A cluster with this name already exists.",
    val NOT_COPIED: String = "This embed was not copied from another message.",
    val MISSING_OPTIONAL_EMBED: String = "Please load an embed or specify one explicitly.",
    val MISSING_EMBED: String = "No embed loaded!",
    val EMPTY_EMBED: String = "Cannot build an empty embed."
)