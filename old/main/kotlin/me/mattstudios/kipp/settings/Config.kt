package me.mattstudios.kipp.settings

import me.mattstudios.config.SettingsManager
import me.mattstudios.config.properties.Property
import java.io.File

/**
 * @author Matt
 */
class Config {

    private val settingsManager = SettingsManager
        .from(File("config", "config.yml"))
        .configurationData(Setting.javaClass)
        .create()

    /**
     * Gets the config property
     */
    operator fun <T> get(property: Property<T>): T {
        return settingsManager[property]
    }

    /**
     * Sets the config property
     */
    operator fun <T: Any> set(property: Property<T>, value: T) {
        settingsManager.set(property, value)
        settingsManager.save()
    }

}