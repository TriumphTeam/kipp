package me.mattstudios.kipp.commands.admin

import me.mattstudios.kipp.utils.Embed
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.annotations.Requirement
import me.mattstudios.mfjda.base.CommandBase

/**
 * @author Matt
 */
@Prefix("!")
@Command("purge")
class Purge : CommandBase() {

    @Default
    @Requirement("#admin-up")
    fun purge(amount: Int?) {
        if (amount == null) return
        if (amount > 99) return

        message.channel.purgeMessages(message.channel.history.retrievePast(amount + 1).complete())

        val builder = Embed(message.author)
                .field("Purge successful!", "• Removed $amount messages.", false)
                .color("#72d6bf")
        message.channel.sendMessage(builder.build()).queue()
    }

}