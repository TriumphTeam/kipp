package me.mattstudios.kipp.listeners

import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.scanner.PasteScanner
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.utils.Color
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.kipp.utils.Utils
import me.mattstudios.kipp.utils.Utils.append
import me.mattstudios.kipp.utils.Utils.extractLinks
import me.mattstudios.kipp.utils.Utils.isPaste
import me.mattstudios.kipp.utils.Utils.plural
import me.mattstudios.kipp.utils.Utils.readContent
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

        val newPastes = mutableListOf<String>()
        var leakDetected = false

        for (link in links) {
            if (!link.isPaste()) continue

            val pasteUrl = link.append("/raw")

            val pasteContent = pasteUrl.readContent()

            if (!leakDetected) leakDetected = leakScanner.searchForLeaks(pasteContent, message, link)
            else continue

            if (link.host.isPasteBin()) {
                newPastes.add(Utils.createPaste(pasteContent))
            }

            // TODO other detections here
        }

        if (newPastes.isEmpty()) return

        val embed = Embed()
                .color(Color.SUCCESS)
                .field("Paste".plural(newPastes.size) + " converted!", newPastes.joinToString("\n"))
                .footer("Please don't use Pastebin, the light theme is terrible!")

        message.textChannel.queueMessage(embed.build())

    }

    /**
     * Checks if the paste is pastebin
     */
    private fun String.isPasteBin() = "pastebin" in this

}