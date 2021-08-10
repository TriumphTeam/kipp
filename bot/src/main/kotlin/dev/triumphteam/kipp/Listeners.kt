package dev.triumphteam.kipp

import dev.triumphteam.bukkit.dsl.TriumphDsl
import dev.triumphteam.bukkit.feature.ApplicationFeature
import dev.triumphteam.bukkit.feature.attribute.key
import dev.triumphteam.bukkit.feature.featureOrNull
import dev.triumphteam.bukkit.feature.install
import dev.triumphteam.jda.JdaApplication
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.hooks.EventListener

class Listeners(private val jda: JDA) {

    fun register(vararg listener: EventListener) {
        jda.addEventListener(listener)
    }

    companion object Feature : ApplicationFeature<JdaApplication, Listeners, Listeners> {

        override val key = key<Listeners>("listeners")

        override fun install(application: JdaApplication, configure: Listeners.() -> Unit): Listeners {
            return Listeners(application.jda)
        }
    }
}

@TriumphDsl
fun JdaApplication.listeners(configuration: Listeners.() -> Unit): Listeners =
    featureOrNull(Listeners)?.apply(configuration) ?: install(Listeners, configuration)

