package me.mattstudios.kipp.settings

import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer.newProperty

object Setting : SettingsHolder {

    @JvmField
    val TOKEN: Property<String> = newProperty("token", "none")

    @JvmField
    val JOIN_CHANNEL: Property<String> = newProperty("join-channel", "0")

    @JvmField
    val DEFAULT_ROLE: Property<String> = newProperty("default-role", "0")

}