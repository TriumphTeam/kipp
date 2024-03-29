package me.mattstudios.kipp.commands.member

import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.base.CommandBase

/**
 * @author Matt
 */
@Prefix("!")
@Command("help")
class Help : CommandBase() {

    @Default
    fun help() {
        message.textChannel.queueMessage(
                Embed(message.author)
                        .field(
                                "Available commands:",
                                "**!paste** - Gives you the link to HelpChat's paste\n" +
                                "**!todo** - Display Matt's TO-DO list\n" +
                                "**!whois** - Displays information about a player\n" +
                                "**?faq** - Shows a list of available FAQs"
                        )
                        .build()
        )
    }

}