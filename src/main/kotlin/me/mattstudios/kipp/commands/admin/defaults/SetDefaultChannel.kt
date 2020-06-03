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
@Command("setchannel")
class SetDefaultChannel(
        private val config: Config,
        private val cache: Cache
) : CommandBase() {

    @Default
    @Requirement("#admin-up")
    fun execute(type: String, channel: TextChannel?) {
        if (channel == null) return

        when (type.toLowerCase()) {

            "join" -> {
                config[Setting.JOIN_LOG_CHANNEL] = channel.id
                cache.joinChannel = channel
                message.textChannel.queueMessage("Join/Quit log channel set successfully!")
            }

            "leak" -> {
                config[Setting.LEAK_LOG_CHANNEL] = channel.id
                cache.leakChannel = channel
                message.textChannel.queueMessage("Leak log channel set successfully!")
            }

            "messages" -> {
                config[Setting.MESSAGE_LOG_CHANNEL] = channel.id
                cache.messageChannel = channel
                message.textChannel.queueMessage("Message log channel set successfully!")
            }

            "reminder" -> {
                config[Setting.REMINDER_CHANNEL] = channel.id
                cache.reminderChannel = channel
                message.textChannel.queueMessage("Reminder channel set successfully!")
            }

            "suggestions" -> {
                config[Setting.SUGGESTIONS_CHANNEL] = channel.id
                cache.suggestionsChannel = channel
                message.textChannel.queueMessage("Suggestions channel set successfully!")
            }

            "bugs" -> {
                config[Setting.BUGS_CHANNEL] = channel.id
                cache.bugsChannel = channel
                message.textChannel.queueMessage("Bugs channel set successfully!")
            }
        }

    }

}