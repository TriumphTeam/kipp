package dev.triumphteam.kipp.event

import dev.triumphteam.bukkit.dsl.TriumphDsl
import dev.triumphteam.bukkit.feature.ApplicationFeature
import dev.triumphteam.bukkit.feature.attribute.key
import dev.triumphteam.bukkit.feature.featureOrNull
import dev.triumphteam.bukkit.feature.install
import dev.triumphteam.jda.JdaApplication
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent

class Listeners(private val jda: JDA, val eventManager: EventManager) {

    @Suppress("UNCHECKED_CAST")
    inline fun <reified E : GenericEvent> on(noinline listener: E.() -> Unit) {
        eventManager.addListener(EventExecutor(E::class.java, listener as GenericEvent.() -> Unit))
    }

    fun register(listener: Listeners.() -> Unit) {
        listener(this)
    }

    companion object Feature : ApplicationFeature<JdaApplication, Listeners, Listeners> {

        override val key = key<Listeners>("listeners")

        override fun install(application: JdaApplication, configure: Listeners.() -> Unit): Listeners {
            val eventManager = EventManager()
            application.jda.setEventManager(eventManager)
            return Listeners(application.jda, eventManager)
        }
    }
}

@TriumphDsl
fun JdaApplication.listeners(configuration: Listeners.() -> Unit): Listeners =
    featureOrNull(Listeners)?.apply(configuration) ?: install(Listeners, configuration).apply(configuration)

