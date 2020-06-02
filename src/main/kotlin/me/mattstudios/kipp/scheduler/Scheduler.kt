package me.mattstudios.kipp.scheduler

import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.kipp.utils.Utils
import net.dv8tion.jda.api.JDA
import java.util.Date
import java.util.Timer
import java.util.concurrent.TimeUnit


/**
 * @author Matt
 */
class Scheduler(jda: JDA, config: Config, cache: Cache) {

    private val regex = Regex("\\[([^]]*)] (.*)")

    init {
        for (reminder in config[Setting.REMINDERS]) {
            val (dateUser, task) = regex.matchEntire(reminder)?.destructured ?: continue

            val (dateArg, userId) = dateUser.split("|")
            val user = jda.getUserById(userId) ?: continue

            val date = Utils.dateFormat.parse(dateArg)
            val channel = cache.reminderChannel ?: continue
            scheduleTask(date) {
                channel.queueMessage(
                        Embed().description(
                                "Hey ${user.asMention} reminding you to:\n" +
                                "```\n" +
                                task +
                                "\n```")
                                .build()
                )

                val reminders = mutableListOf<String>()
                reminders.addAll(config[Setting.REMINDERS])
                reminders.remove(reminder)
                config[Setting.REMINDERS] = reminders
            }
        }
    }

    /**
     * Schedules a single run task at a specific date
     */
    fun scheduleTask(date: Date, task: () -> Unit) {
        val kippTask = KippTask(task)
        val timer = Timer()
        timer.schedule(kippTask, date)
    }

    /**
     * Schedules a repeating task at a specific date that will run every "period" in a time unit
     */
    fun scheduleRepeatingTask(date: Date, period: Int, timeUnit: TimeUnit = TimeUnit.DAYS, task: () -> Unit) {
        val kippTask = KippTask(task)
        val timer = Timer()
        timer.schedule(kippTask, date, TimeUnit.MILLISECONDS.convert(period.toLong(), timeUnit))
    }

}