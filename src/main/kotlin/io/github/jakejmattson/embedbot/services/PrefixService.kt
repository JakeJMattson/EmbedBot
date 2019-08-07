package io.github.jakejmattson.embedbot.services

import io.github.jakejmattson.embedbot.dataclasses.Configuration
import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.api.dsl.KConfiguration

@Service
class PrefixService(private val configuration: Configuration, private val kConfiguration: KConfiguration) {
    init {
        setPrefix(configuration.prefix)
    }

    fun setPrefix(prefix: String) {
        configuration.prefix = prefix
        kConfiguration.prefix = prefix
    }
}