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
@Command("paste")
class Paste : CommandBase() {

    @Default
    fun paste() {
        val embed = Embed(message.author).field("HelpChat's paste", "Please use [**this paste**](https://paste.helpch.at/) instead.")
        message.textChannel.queueMessage(embed.build())
    }

}