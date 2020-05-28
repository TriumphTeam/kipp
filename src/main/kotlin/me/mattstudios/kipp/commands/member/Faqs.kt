package me.mattstudios.kipp.commands.member

import me.mattstudios.kipp.manager.FaqManager
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.base.CommandBase

/**
 * @author Matt
 */
@Prefix("!")
@Command("faq")
class Faqs(private val faqManager: FaqManager) : CommandBase() {

    @Default
    fun faq() {
        val faqMessage = Embed(message.author).field("Available FAQs:", faqManager.getFaqs().joinAndAppend()).build()
        message.channel.sendMessage(faqMessage).queue()
    }

    /**
     * Appends the list with `?` around and joins it with ", "
     */
    private fun List<String>.joinAndAppend(): String = this.joinToString(", ") { "`?$it`" }

}