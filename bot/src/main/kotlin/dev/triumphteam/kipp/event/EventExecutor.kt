package dev.triumphteam.kipp.event

import net.dv8tion.jda.api.events.GenericEvent

class EventExecutor<E : GenericEvent>(val eventClass: Class<E>, private val listener: E.() -> Unit) {

    fun execute(event: E) {
        listener(event)
    }

}