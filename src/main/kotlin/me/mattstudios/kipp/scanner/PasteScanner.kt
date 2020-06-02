package me.mattstudios.kipp.scanner

import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.kipp.utils.Color
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.kipp.utils.Utils.readContent
import net.dv8tion.jda.api.entities.Message
import java.net.URL

/**
 * @author Matt
 */
class PasteScanner(
        private val config: Config,
        private val cache: Cache
) {

    /**
     * Handles the leak search for the paste
     */
    fun searchForLeaks(paste: URL, message: Message, originalLink: URL): Boolean {
        val channel = cache.leakChannel ?: return false
        val leakWords = config[Setting.LEAK_WORDS]

        val pasteContent = paste.readContent()
        val lines = pasteContent.split("\n")

        val index = lines.findLeak(leakWords)

        if (index == -1) return false

        val embed = Embed().color("#ffc180").title("Potential leak by ${message.author.asTag} has been detected!")
                .color(Color.WARNING)
                .field(
                        "Leak was triggered by line: **${index + 1}**",
                        "```ini\n" +
                        lines[index] +
                        "\n```"
                )
                .field("Paste", originalLink.toString())
                .field("User", message.author.asMention)
                .field("Channel", "[**#${message.textChannel.name}**](${message.jumpUrl})")

        channel.queueMessage(embed.build())

        return true
    }

    /**
     * Finds a leak on the list
     */
    private fun List<String>.findLeak(words: List<String>): Int {
        for (line in this) {
            if (words.any { it in line }) return indexOf(line)
        }

        return -1
    }

}
