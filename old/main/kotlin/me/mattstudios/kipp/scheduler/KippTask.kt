package me.mattstudios.kipp.scheduler

import java.util.TimerTask

/**
 * @author Matt
 */
class KippTask(private val task: () -> Unit) : TimerTask() {

    override fun run() = task.invoke()

}