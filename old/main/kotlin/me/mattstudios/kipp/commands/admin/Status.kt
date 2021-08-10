package me.mattstudios.kipp.commands.admin

import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.base.CommandBase
import java.util.concurrent.TimeUnit

/**
 * @author Matt
 */
@Prefix("!")
@Command("status")
class Status(private val startTime: Long) : CommandBase() {

    @Default
    fun status() {
        val kipp = jda.selfUser

        val uptime = (System.currentTimeMillis() - startTime).formatTime()
        val embed = Embed(message.author).author("Kipp v1.0", kipp.avatarUrl ?: kipp.defaultAvatarUrl)
                .field("Status:", "<:online:718229424948117536> Operational", true)
                .field("Uptime:", uptime, true)
                .field("Creator:", "Matt#7079", true)

        message.textChannel.queueMessage(embed.build())
    }

    private fun Long.formatTime(): String {
        var timeMillis = this
        val days = TimeUnit.MILLISECONDS.toDays(timeMillis)
        timeMillis -= TimeUnit.DAYS.toMillis(days)
        val hours = TimeUnit.MILLISECONDS.toHours(timeMillis)
        timeMillis -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMillis)
        timeMillis -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeMillis)

        val builder = StringBuilder()

        if (days == 1L) builder.append("$days day ")
        else if (days != 0L) builder.append("$days days ")

        if (hours == 1L) builder.append("$hours hour ")
        else if (hours != 0L) builder.append("$hours hours ")

        if (minutes == 1L) builder.append("$minutes min ")
        else if (minutes != 0L) builder.append("$minutes mins ")

        if (seconds == 1L) builder.append("$seconds sec")
        else if (seconds != 0L) builder.append("$seconds secs")

        return builder.toString()
    }

}