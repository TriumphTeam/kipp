package dev.triumphteam.kipp.func

import dev.triumphteam.core.feature.feature
import dev.triumphteam.core.jda.JdaApplication
import dev.triumphteam.kipp.config.Config
import dev.triumphteam.kipp.config.KippColor
import dev.triumphteam.kipp.config.Settings
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val LOGGER: Logger = LoggerFactory.getLogger("dev.triumphteam.kipp")

fun log(message: () -> String) {
    return LOGGER.info(message())
}

fun JdaApplication.kippInfo(message: () -> String) {
    return kippLog(KippColor.DEFAULT, message)
}

fun JdaApplication.kippLog(color: KippColor, message: () -> String) {
    val config = feature(Config)
    val kippChannel = jda.getTextChannelById(config[Settings.CHANNELS].kipp) ?: return

    kippChannel.queueMessage(
        embed {
            description(
                """
                    ```
                    ${message()}
                    ```
                """.trimIndent()
            )
            color(color)
        }
    )
}