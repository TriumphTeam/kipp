package dev.triumphteam.kipp.event

import dev.triumphteam.bukkit.dsl.TriumphDsl
import dev.triumphteam.bukkit.feature.ApplicationFeature
import dev.triumphteam.bukkit.feature.attribute.key
import dev.triumphteam.bukkit.feature.featureOrNull
import dev.triumphteam.bukkit.feature.install
import dev.triumphteam.jda.JdaApplication
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.GenericEvent

class Listeners(private val jdaApplication: JdaApplication) {

    val jda = jdaApplication.jda

    fun register(listener: JdaApplication.() -> Unit) {
        listener(jdaApplication)
    }

    companion object Feature : ApplicationFeature<JdaApplication, Listeners, Listeners> {

        override val key = key<Listeners>("listeners")

        override fun install(application: JdaApplication, configure: Listeners.() -> Unit): Listeners {
            application.jda.setEventManager(EventManager())
            return Listeners(application)
        }
    }
}

@TriumphDsl
fun JdaApplication.listen(listener: JdaApplication.() -> Unit): Listeners {
    val feature = featureOrNull(Listeners) ?: install(Listeners)
    feature.register(listener)
    return feature
}

@TriumphDsl
@Suppress("UNCHECKED_CAST")
inline fun <reified E : GenericEvent> JdaApplication.on(noinline listener: E.() -> Unit) {
    jda.addEventListener(EventExecutor(E::class.java, listener as GenericEvent.() -> Unit))
}

@TriumphDsl
@Suppress("UNCHECKED_CAST")
inline fun <reified E : GenericEvent> JDABuilder.on(noinline listener: E.() -> Unit) {
    addEventListeners(EventExecutor(E::class.java, listener as GenericEvent.() -> Unit))
}