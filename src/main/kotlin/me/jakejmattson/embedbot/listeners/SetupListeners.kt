package me.jakejmattson.embedbot.listeners

import com.google.common.eventbus.Subscribe
import me.jakejmattson.embedbot.dataclasses.Configuration
import net.dv8tion.jda.api.events.guild.GuildJoinEvent

class SetupListeners(private val config: Configuration) {
    @Subscribe
    fun onJoin(event: GuildJoinEvent) = config.setup(event.guild)
}