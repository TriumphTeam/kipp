package me.mattstudios.kipp.commands.admin.defaults

import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Delete
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.base.CommandBase

/**
 * @author Matt
 */
@Prefix("!")
@Command("setsettings")
class SetSettings(private val config: Config, private val cache: Cache) : CommandBase() {

    @Default
    @Delete
    fun settings(messageId: String) {

        val settingsMessage = message.textChannel.retrieveMessageById(messageId).complete() ?: return

        val ccmdEmote = cache.ccmdEmote
        val mfEmote = cache.mfEmote
        val mattEmote = cache.mattEmote

        if (ccmdEmote != null) settingsMessage.addReaction(ccmdEmote).queue()
        if (mfEmote != null) settingsMessage.addReaction(mfEmote).queue()
        if (mattEmote != null) settingsMessage.addReaction(mattEmote).queue()

        config[Setting.SETTINGS_MESSAGE] = messageId
    }

}