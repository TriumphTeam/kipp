package me.mattstudios.kipp.scheduler

import java.util.TimerTask

/**
 * @author Matt
 */
@FunctionalInterface
interface Task {

    fun run(task: TimerTask)

}