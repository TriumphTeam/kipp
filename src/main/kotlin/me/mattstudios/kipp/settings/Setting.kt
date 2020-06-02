package me.mattstudios.kipp.settings

import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer.newListProperty
import ch.jalu.configme.properties.PropertyInitializer.newProperty

object Setting : SettingsHolder {

    @JvmField
    val TOKEN: Property<String> = newProperty("token", "none")

    @JvmField
    val JOIN_LOG_CHANNEL: Property<String> = newProperty("channel.join-log", "0")

    @JvmField
    val LEAK_LOG_CHANNEL: Property<String> = newProperty("channel.leak-log", "0")

    @JvmField
    val MESSAGE_LOG_CHANNEL: Property<String> = newProperty("channel.message-log", "0")

    @JvmField
    val REMINDER_CHANNEL: Property<String> = newProperty("channel.reminder", "0")

    @JvmField
    val MEMBER_ROLE: Property<String> = newProperty("role.member", "0")

    @JvmField
    val PLUGINS_ROLE: Property<String> = newProperty("role.plugins", "0")

    @JvmField
    val PINGS_ROLE: Property<String> = newProperty("role.pings", "0")

    @JvmField
    val ADMIN_ROLE: Property<String> = newProperty("role.admin", "496353695605456897")

    @JvmField
    val SQL_HOST: Property<String> = newProperty("database.host", "localhost")

    @JvmField
    val SQL_USER: Property<String> = newProperty("database.user", "matt")

    @JvmField
    val SQL_PASSWORD: Property<String> = newProperty("database.password", "bleh")

    @JvmField
    val SQL_DATABASE: Property<String> = newProperty("database.database", "matt")

    @JvmField
    val DIALOGFLOW_PROJECT: Property<String> = newProperty("dialogflow-project", "")

    @JvmField
    val FAQ_COMMANDS: Property<MutableList<String>> = newListProperty("faq-commands")

    @JvmField
    val TODOS: Property<MutableList<String>> = newListProperty("todos")

    @JvmField
    val LEAK_WORDS: Property<MutableList<String>> = newListProperty("leak-words")

    @JvmField
    val BLACK_LISTED_CHANNELS: Property<MutableList<String>> = newListProperty("black-listed-channels")

    @JvmField
    val REMINDERS: Property<MutableList<String>> = newListProperty("reminders")

}
