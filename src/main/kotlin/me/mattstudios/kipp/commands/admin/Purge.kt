package me.mattstudios.kipp.commands.admin

import me.mattstudios.kipp.utils.Utils.hexToRgb
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.annotations.Requirement
import me.mattstudios.mfjda.base.CommandBase
import net.dv8tion.jda.api.EmbedBuilder

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

        val builder = EmbedBuilder()
                .addField("Purge successful!", "• Removed $amount messages.", false)
                .setColor(hexToRgb("#72d6bf"))
        message.channel.sendMessage(builder.build()).queue()
    }

}