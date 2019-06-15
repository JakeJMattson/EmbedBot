package io.github.jakejmattson.embedbot.services

import io.github.jakejmattson.embedbot.dataclasses.Configuration
import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.api.dsl.KJDAConfiguration

@Service
class PrefixService(configuration: Configuration, private val kjdaConfiguration: KJDAConfiguration) {
    init {
        setPrefix(configuration.prefix)
    }

    fun setPrefix(prefix: String) {
        kjdaConfiguration.prefix = prefix
    }
}