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

    @JvmField
    val SQL_HOST: Property<String> = newProperty("database.host", "localhost")

    @JvmField
    val SQL_USER: Property<String> = newProperty("database.user", "matt")

    @JvmField
    val SQL_PASSWORD: Property<String> = newProperty("database.password", "bleh")

    @JvmField
    val SQL_DATABASE: Property<String> = newProperty("database.database", "matt")

}