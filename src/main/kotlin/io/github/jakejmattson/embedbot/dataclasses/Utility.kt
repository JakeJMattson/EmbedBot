package io.github.jakejmattson.embedbot.dataclasses

import java.io.File

private class Utility

data class CopyLocation(val channelId: String, val messageId: String) {
    override fun toString() = "Channel ID: $channelId\nMessage ID: $messageId"
}

data class OperationResult(val wasSuccessful: Boolean, val message: String)

infix fun Boolean.withMessage(message: String) = OperationResult(this, message)

fun getFileSystemLocation() = File(Utility::class.java.protectionDomain.codeSource.location.toURI())