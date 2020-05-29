package me.mattstudios.kipp.listeners

import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.scanners.Scanner
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.utils.Utils.extractLinks
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.net.URL

/**
 * @author Matt
 */
class MessagePasteListener(config: Config, cache: Cache) : ListenerAdapter() {

    // TODO add more services
    private val pasteServices = listOf("pastebin.com", "hastebin.com", "paste.helpch.at")

    // Scanners for content
    private val leakScanner = Scanner(config, cache)

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

    /**
     * Checks if the link is a paste or not
     */
    private fun URL.isPaste() = pasteServices.any { it in this.host }
    private fun URL.append(string: String): URL = if (this.path == null || "/raw" in this.path) this else URL(this, string + this.path)

}