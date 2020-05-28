package me.mattstudios.kipp.commands.admin

import me.mattstudios.kipp.manager.FaqManager
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.annotations.Requirement
import me.mattstudios.mfjda.annotations.SubCommand
import me.mattstudios.mfjda.base.CommandBase

/**
 * @author Matt
 */
@Prefix("?")
@Command("faqs")
class FaqsManage(private val faqManager: FaqManager) : CommandBase() {

    @SubCommand("create")
    @Requirement("#admin-up")
    fun createFaq(command: String, title: String, args: Array<String>) {
        faqManager.createFaq(command, title, args.joinToString(" "))
        val completeMessage = Embed(message.author).field("FAQ created successfully!", "The new FAQ is `?$command`.").build()
        message.channel.sendMessage(completeMessage).queue()
    }

    @SubCommand("remove")
    @Requirement("#admin-up")
    fun deleteFaq(command: String) {
        val removeEmbed = Embed(message.author)

        if (faqManager.delete(command)) {
            removeEmbed.field("FAQ delete", "The FAQ `?$command` was deleted successfully!")
        } else {
            removeEmbed.field("FAQ delete", "The FAQ `?$command` does not exist or an error occurred while deleting it!")
        }

        message.channel.sendMessage(removeEmbed.build()).queue()
    }

}