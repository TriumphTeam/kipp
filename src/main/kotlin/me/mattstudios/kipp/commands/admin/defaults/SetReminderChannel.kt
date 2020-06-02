package me.mattstudios.kipp.commands.admin.defaults

import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.annotations.Requirement
import me.mattstudios.mfjda.base.CommandBase
import net.dv8tion.jda.api.entities.TextChannel

/**
 * @author Matt
 */
@Prefix("!")
@Command("setreminderchannel")
class SetReminderChannel(private val config: Config, private val cache: Cache) : CommandBase() {

    @Default
    @Requirement("#admin-up")
    fun setJoinChannel(channel: TextChannel?) {
        if (channel == null) return
        config[Setting.REMINDER_CHANNEL] = channel.id
        cache.reminderChannel = channel

        message.textChannel.queueMessage("Reminder channel set successfully!")
    }

}