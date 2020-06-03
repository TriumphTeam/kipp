package me.mattstudios.kipp.commands.admin

import com.google.gson.Gson
import me.mattstudios.kipp.data.Database
import me.mattstudios.kipp.data.JsonEmbed
import me.mattstudios.kipp.manager.FaqManager
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.kipp.utils.Utils.append
import me.mattstudios.kipp.utils.Utils.isPaste
import me.mattstudios.kipp.utils.Utils.readContent
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.annotations.Requirement
import me.mattstudios.mfjda.annotations.SubCommand
import me.mattstudios.mfjda.base.CommandBase
import java.net.URL

/**
 * @author Matt
 */
@Prefix("?")
@Command("faq")
class FaqsManage(
        private val faqManager: FaqManager,
        private val database: Database
) : CommandBase() {

    private val gson = Gson()

    @Default
    fun faq() {
        val faqMessage = Embed(message.author).field("Available FAQs:", faqManager.getFaqs().joinAndAppend()).build()
        message.textChannel.queueMessage(faqMessage)
    }

    @SubCommand("create")
    @Requirement("#admin-up")
    fun createFaq(link: URL?) {
        message.channel.queueMessage("create")
        if (link == null) return
        if (!link.isPaste()) return
        val paste = link.append("/raw")

        val pasteContent = paste.readContent()

        val jsonEmbed = gson.fromJson(pasteContent, JsonEmbed::class.java)

        //GlobalScope.launch { database.insertFaq(jsonEmbed.command, jsonEmbed, message.author) }

        /*faqManager.createFaq(jsonEmbed)

        message.textChannel.queueMessage(
                Embed(message.author)
                        .color(Color.SUCCESS)
                        .field("FAQ created successfully!", "The new FAQ is `?${jsonEmbed.command}`.")
                        .build()
        )*/
    }

    @SubCommand("remove")
    @Requirement("#admin-up")
    fun deleteFaq(command: String) {
        message.channel.queueMessage("remove")
        /*val removeEmbed = Embed(message.author)

        if (faqManager.delete(command)) {
            removeEmbed
                    .color(Color.SUCCESS)
                    .field("FAQ delete", "The FAQ `?$command` was deleted successfully!")
        } else {
            removeEmbed
                    .color(Color.FAIL)
                    .field("FAQ delete", "The FAQ `?$command` does not exist or an error occurred while deleting it!")
        }

        message.textChannel.queueMessage(removeEmbed.build())*/
    }

    /**
     * Appends the list with `?` around and joins it with ", "
     */
    private fun List<String>.joinAndAppend() = this.joinToString(", ") { "`?$it`" }

}