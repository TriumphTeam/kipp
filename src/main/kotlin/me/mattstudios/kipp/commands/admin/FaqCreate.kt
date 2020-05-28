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
class FaqCreate(private val faqManager: FaqManager) : CommandBase() {

    @SubCommand("create")
    @Requirement("#admin-up")
    fun createFaq(command: String, title: String, args: Array<String>) {
        faqManager.createFaq(command, title, args.joinToString(" "))
        val completeMessage = Embed(message.author).field("FAQ created successfully!", "The new FAQ is `?$command`.").build()
        message.channel.sendMessage(completeMessage).queue()
    }

}