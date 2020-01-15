package me.jakejmattson.embedbot.dataclasses

data class OperationResult(val wasSuccessful: Boolean, val message: String)

infix fun Boolean.withMessage(message: String) = OperationResult(this, message)