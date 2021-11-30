package dev.triumphteam.kipp.commands

import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.Command
import dev.triumphteam.cmd.core.annotation.Default
import dev.triumphteam.cmd.core.annotation.Description
import dev.triumphteam.cmd.slash.sender.SlashSender
import dev.triumphteam.kipp.func.embed
import net.dv8tion.jda.api.JDA
import java.util.concurrent.TimeUnit

@Command("about")
@Description("Shows information about the bot")
class AboutCommand(private val jda: JDA) : BaseCommand() {

    private val startTime = System.currentTimeMillis()

    @Default
    fun SlashSender.about() {
        val kipp = jda.selfUser
        val uptime = (System.currentTimeMillis() - startTime).formatTime()
        val embed = embed(timestamp = false) {
            thumbnail(kipp.avatarUrl ?: kipp.defaultAvatarUrl)
            description(
                """
                Hi, I am **Kipp**!
                I was written in [Kotlin](https://kotlinlang.org/) using [JDA](https://github.com/DV8FromTheWorld/JDA).
                My source can be found [here](https://github.com/TriumphTeam/kipp)!
                """.trimIndent()
            )
            field("Status:", "<:online:718229424948117536> Operational", true)
            field("Uptime:", uptime, true)
            footer("Created by: Matt#7079")
        }

        reply(embed).queue()
    }

    /**
     * This is ugly af, but I can't be assed right now
     * Please ignore it
     */
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