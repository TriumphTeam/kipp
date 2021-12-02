package dev.triumphteam.kipp.button

import dev.triumphteam.core.dsl.TriumphDsl
import dev.triumphteam.core.feature.ApplicationFeature
import dev.triumphteam.core.feature.attribute.key
import dev.triumphteam.core.feature.feature
import dev.triumphteam.core.jda.JdaApplication
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * Class that buttons need to extend.
 */
abstract class BaseButton

/**
 * Annotation to identify the button.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Button(val id: String)

/**
 * Annotation to auto defer a button click.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Defer(val ephemeral: Boolean = false)

class Buttons : ListenerAdapter() {

    private val buttons = mutableMapOf<String, ButtonHolder>()

    /**
     * TODO: Maybe move this to a different class.
     */
    override fun onButtonClick(event: ButtonClickEvent): Unit = with(event) {
        val buttonId = button?.id ?: return
        val (key, value) = buttons.map { it.key to it.value }.firstOrNull { buttonId.startsWith(it.first) } ?: return
        val subId = buttonId.removePrefix(key).removePrefix("-")

        if (value.defer) event.deferReply(value.ephemeral).queue()
        value.execute(subId, event)
    }

    private fun register(baseButton: BaseButton) {
        val klass = baseButton.javaClass
        val id = klass.getAnnotation(Button::class.java)?.id
            ?: throw IllegalArgumentException("Button ${klass.simpleName} has no id.")

        val deferAnnotation = klass.getAnnotation(Defer::class.java)
        val ephemeral = deferAnnotation?.ephemeral ?: false
        val defer = deferAnnotation != null

        val buttonHolder = buttons.computeIfAbsent(id) { ButtonHolder(defer, ephemeral) }
        buttonHolder.addAll(baseButton)
    }

    fun register(vararg baseButtons: BaseButton) {
        baseButtons.forEach { register(it) }
    }

    /**
     * Feature companion, for installing the feature.
     */
    companion object Feature : ApplicationFeature<JdaApplication, Buttons, Buttons> {

        override val key = key<Buttons>("Buttons")

        override fun install(application: JdaApplication, configure: Buttons.() -> Unit): Buttons {
            return Buttons().apply(configure).apply { application.jda.addEventListener(this) }
        }
    }
}

/**
 * A holder for all sub buttons of a button.
 */
private class ButtonHolder(val defer: Boolean, val ephemeral: Boolean) {

    private val subButtons = mutableMapOf<String, SubButton>()

    fun addAll(baseButton: BaseButton) {
        baseButton.javaClass.declaredMethods.asSequence().filter { Modifier.isPublic(it.modifiers) }.forEach { method ->
            val subButtonId = method.getAnnotation(Button::class.java)?.id ?: return@forEach

            val parameters = method.parameters
            if (parameters.size != 1 && parameters.first().type != ButtonClickEvent::class.java) {
                throw IllegalArgumentException("Button ${method.name} needs ${ButtonClickEvent::class.java.simpleName}} as a parameter.")
            }

            val deferAnnotation = method.getAnnotation(Defer::class.java)
            val ephemeral = deferAnnotation?.ephemeral ?: false
            val defer = deferAnnotation != null

            subButtons[subButtonId] = SubButton(baseButton, method, defer, ephemeral)
        }
    }

    fun execute(id: String, event: ButtonClickEvent) = subButtons[id]?.execute(event)
}

/**
 * Simple data class for executing the button action.
 */
private data class SubButton(
    private val baseButton: BaseButton,
    private val method: Method,
    private val defer: Boolean,
    private val ephemeral: Boolean
) {

    fun execute(event: ButtonClickEvent) {
        if (defer) event.deferReply(ephemeral).queue()
        method.invoke(baseButton, event)
    }

}

/**
 * Simplifies registering buttons.
 */
@TriumphDsl
fun JdaApplication.buttons(configure: Buttons.() -> Unit): Buttons = feature(Buttons).apply(configure)
