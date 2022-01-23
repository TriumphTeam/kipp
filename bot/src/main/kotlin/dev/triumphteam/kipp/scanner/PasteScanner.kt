package dev.triumphteam.kipp.scanner

import dev.triumphteam.kipp.config.Config
import dev.triumphteam.kipp.config.KippColor
import dev.triumphteam.kipp.config.Settings
import dev.triumphteam.kipp.func.embed
import net.dv8tion.jda.api.entities.Message
import java.net.URL

class PasteScanner(private val config: Config) {

    /**
     * Handles the leak search for the paste
     */
    fun searchForLeaks(pasteContent: String, message: Message, originalLink: URL): Boolean {
        val leakWords = config[Settings.LEAK_WORDS]
        val lines = pasteContent.split("\n")

        val leaks = lines.findLeak(leakWords)
        if (leaks.isEmpty()) return false

        val channel = message.jda.getTextChannelById(config[Settings.CHANNELS].leak) ?: return false

        val embed = embed {
            color("#ffc180")
            title("Potential leak by ${message.author.asTag} has been detected!")
            color(KippColor.WARNING)
            description(
                buildString {
                    append("```ini")
                    append("\n")
                    append(leaks.entries.joinToString("\n\n") { (index, line) -> "$index: $line" })
                    append("\n")
                    append("```")
                }
            )

            fields {
                field("Paste", originalLink.toString())
                field("User", message.author.asMention)
                field("Channel", "[**#${message.textChannel.name}**](${message.jumpUrl})")
            }
        }

        channel.sendMessageEmbeds(embed).queue()
        return true
    }

    /**
     * Finds a leak on the list
     */
    private fun List<String>.findLeak(words: List<String>): Map<Int, String> {
        return withIndex().associate { (index, line) -> index to line }
            .filter { (_, line) -> words.any { it in line } }
    }

}