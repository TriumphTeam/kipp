package me.mattstudios.kipp.listeners

import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.scanner.PasteScanner
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.utils.Utils.append
import me.mattstudios.kipp.utils.Utils.extractLinks
import me.mattstudios.kipp.utils.Utils.isPaste
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

/**
 * @author Matt
 */
class MessagePasteListener(config: Config, cache: Cache) : ListenerAdapter() {

    // Scanners for content
    private val leakScanner = PasteScanner(config, cache)

    /**
     * Handles the receiving of messages
     */
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (event.author.isBot) return

        val message = event.message
        val links = message.contentRaw.extractLinks()

        if (links.isEmpty()) return

        for (link in links) {
            if (!link.isPaste()) continue

            val pasteUrl = link.append("/raw")
            val leakDetected = leakScanner.searchForLeaks(pasteUrl, message, link)

            if (leakDetected) continue

            // TODO other detections here
        }

    }

}