package me.mattstudios.kipp.listeners

import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.utils.Utils.extractLinks
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.net.URL

/**
 * @author Matt
 */
class LeakListener(private val config: Config) : ListenerAdapter() {

    val pasteServices = listOf("pastebin.com", "hastebin.com", "paste.helpch.at")

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (event.author.isBot) return

        val message = event.message
        val links = message.contentRaw.extractLinks()

        if (links.isEmpty()) return

        for (link in links) {

            if (link.isPaste()) {
                event.channel.sendMessage("Paste detected!").queue()
            }

        }

    }

    private fun URL.isPaste() = pasteServices.any { it in this.host }

}