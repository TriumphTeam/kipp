package me.mattstudios.kipp.scheduler

import java.util.Date
import java.util.Timer
import java.util.concurrent.TimeUnit


/**
 * @author Matt
 */
class Scheduler {
    // Map to hold the tasks
    private val tasks = mutableMapOf<String, Timer>()

    /**
     * Schedules a single run task at a specific date
     */
    fun scheduleTask(id: String, date: Date, task: () -> Unit) {
        val kippTask = KippTask(task)
        val timer = Timer()
        timer.schedule(kippTask, date)
        tasks[id] = timer
    }

    /**
     * Schedules a repeating task at a specific date that will run every "period" in a time unit
     */
    fun scheduleRepeatingTask(id: String, date: Date, period: Int, timeUnit: TimeUnit = TimeUnit.DAYS, task: () -> Unit) {
        val kippTask = KippTask(task)
        val timer = Timer()
        timer.schedule(kippTask, date, TimeUnit.MILLISECONDS.convert(period.toLong(), timeUnit))
        tasks[id] = timer
    }

    /**
     * Cancels the specified task if it exists
     */
    fun cancelTask(id: String) {
        tasks[id]?.cancel()
        tasks.remove(id)
    }

    /**
     * Cancels all the tasks stored
     */
    fun cancelAll() {
        tasks.forEach { (_, timer) -> timer.cancel() }
        tasks.clear()
    }
}