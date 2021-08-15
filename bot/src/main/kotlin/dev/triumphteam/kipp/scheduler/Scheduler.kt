package dev.triumphteam.kipp.scheduler

import dev.triumphteam.bukkit.dsl.TriumphDsl
import dev.triumphteam.bukkit.feature.ApplicationFeature
import dev.triumphteam.bukkit.feature.attribute.key
import dev.triumphteam.bukkit.feature.featureOrNull
import dev.triumphteam.bukkit.feature.install
import dev.triumphteam.jda.JdaApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class Scheduler {

    private val scope = CoroutineScope(Dispatchers.IO)

    /**
     * Schedules a single run task at a specific date
     */
    fun scheduleTask(date: Date, task: () -> Unit) {
        scope.launch {
            val kippTask = KippTask(task)
            val timer = Timer()
            timer.schedule(kippTask, date)
        }
    }

    /**
     * Schedules a repeating task at a specific date that will run every "period" in a time unit
     */
    fun scheduleRepeatingTask(period: Long, delay: Long, timeUnit: TimeUnit = TimeUnit.DAYS, task: () -> Unit) {
        scope.launch {
            Timer().schedule(KippTask(task), timeUnit.toMillis(delay), timeUnit.toMillis(period))
        }
    }

    companion object Feature : ApplicationFeature<JdaApplication, Scheduler, Scheduler> {

        override val key = key<Scheduler>("scheduler")

        override fun install(application: JdaApplication, configure: Scheduler.() -> Unit): Scheduler {
            return Scheduler()
        }
    }
}

class KippTask(private val task: () -> Unit) : TimerTask() {

    override fun run() = task()
}

@TriumphDsl
fun JdaApplication.repeatingTask(
    period: Duration,
    delay: Duration = period,
    task: () -> Unit
): Scheduler {
    val scheduler = featureOrNull(Scheduler) ?: install(Scheduler)
    scheduler.scheduleRepeatingTask(period.duration, delay.duration, TimeUnit.MILLISECONDS, task)
    return scheduler
}