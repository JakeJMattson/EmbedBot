package me.jakejmattson.embedbot.listeners

import com.google.common.eventbus.Subscribe
import me.jakejmattson.embedbot.conversations.SetupConversation
import me.jakejmattson.embedbot.dataclasses.Configuration
import me.jakejmattson.kutils.api.services.ConversationService
import net.dv8tion.jda.api.events.guild.GuildJoinEvent

class JoinListener(private val config: Configuration, private val conversationService: ConversationService) {
    @Subscribe
    fun onJoin(event: GuildJoinEvent) {
        val guild = event.guild
        val guildConfig = config.getGuildConfig(guild.id)

        if (guildConfig != null)
            return

        val owner = guild.retrieveOwner().complete().user

        conversationService.startPrivateConversation<SetupConversation>(owner, guild)
    }
}