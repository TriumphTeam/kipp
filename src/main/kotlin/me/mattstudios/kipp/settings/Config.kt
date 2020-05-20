package me.mattstudios.kipp.settings

import ch.jalu.configme.SettingsManagerBuilder
import ch.jalu.configme.properties.Property
import java.io.File

/**
 * @author Matt
 */
class Config {

    private val settingsManager = SettingsManagerBuilder.withYamlFile(File("config", "config.yml")).configurationData(Setting.javaClass).useDefaultMigrationService().create()

    /**
     * Gets the config property
     */
    operator fun <T> get(property: Property<T>): T {
        return settingsManager.getProperty(property)
    }

    /**
     * Gets the config property
     */
    operator fun <T> set(property: Property<T>, value: T) {
        settingsManager.setProperty(property, value)
        settingsManager.save()
    }

}