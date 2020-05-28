package me.mattstudios.kipp.settings

import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer.newListProperty
import ch.jalu.configme.properties.PropertyInitializer.newProperty

object Setting : SettingsHolder {

    @JvmField
    val TOKEN: Property<String> = newProperty("token", "none")

    @JvmField
    val JOIN_LOG_CHANNEL: Property<String> = newProperty("join-log-channel", "0")

    @JvmField
    val MEMBER_ROLE: Property<String> = newProperty("member-role", "0")

    @JvmField
    val PLUGINS_ROLE: Property<String> = newProperty("plugins-role", "0")

    @JvmField
    val PINGS_ROLE: Property<String> = newProperty("pings-role", "0")

    @JvmField
    val SQL_HOST: Property<String> = newProperty("database.host", "localhost")

    @JvmField
    val SQL_USER: Property<String> = newProperty("database.user", "matt")

    @JvmField
    val SQL_PASSWORD: Property<String> = newProperty("database.password", "bleh")

    @JvmField
    val SQL_DATABASE: Property<String> = newProperty("database.database", "matt")

    @JvmField
    val FAQ_COMMANDS: Property<MutableList<String>> = newListProperty("faq-commands")

}