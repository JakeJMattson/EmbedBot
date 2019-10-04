package io.github.jakejmattson.embedbot.listeners

import com.google.common.eventbus.Subscribe
import io.github.jakejmattson.embedbot.services.InfoService
import me.aberrantfox.kjdautils.extensions.stdlib.trimToID
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class MessageListener(private val infoService: InfoService) {
    @Subscribe
    fun onMessageReceived(event: GuildMessageReceivedEvent) {
        with (event) {
            if (author.isBot) return

            if (message.contentRaw.trimToID() == channel.jda.selfUser.id)
                channel.sendMessage(infoService.botInfo(guild)).queue()
        }
    }
}