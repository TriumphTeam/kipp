package dev.triumphteam.kipp.event

import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.hooks.IEventManager
import java.util.concurrent.ConcurrentHashMap

@Suppress("UNCHECKED_CAST")
class EventManager : IEventManager {

    private val listeners = ConcurrentHashMap<Class<out GenericEvent>, MutableList<EventExecutor<in GenericEvent>>>()

    inline fun <reified E : GenericEvent> register(noinline listener: E.() -> Unit) {
        addListener(EventExecutor(E::class.java, listener as GenericEvent.() -> Unit))
    }

    override fun handle(event: GenericEvent) {
        val executors = listeners[event::class.java] ?: return
        executors.forEach { executor ->
            executor.execute(event)
        }
    }

    override fun register(listener: Any) {
        if (listener !is EventExecutor<*>) return
        addListener(listener)
    }

    override fun unregister(listener: Any) {
        TODO("No need for this for now.")
    }

    override fun getRegisteredListeners(): List<Any> {
        return listeners.entries.toList()
    }

    fun addListener(executor: EventExecutor<*>) {
        with(executor.eventClass) {
            val executors = listeners[this] ?: mutableListOf()
            executors.add(executor as EventExecutor<in GenericEvent>)
            listeners[this] = executors
        }
    }

}