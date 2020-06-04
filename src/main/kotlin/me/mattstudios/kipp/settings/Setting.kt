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
    val SUGGESTIONS_CHANNEL: Property<String> = newProperty("channel.suggestions", "0")

    @JvmField
    val BUGS_CHANNEL: Property<String> = newProperty("channel.bugs", "0")

    @JvmField
    val MEMBER_ROLE: Property<String> = newProperty("role.member", "0")

    @JvmField
    val PLUGINS_ROLE: Property<String> = newProperty("role.plugins", "0")

    @JvmField
    val SUBSCRIPTIONS_ROLE: Property<String> = newProperty("role.subscriptions", "0713927697679777802")

    @JvmField
    val ADMIN_ROLE: Property<String> = newProperty("role.admin", "496353695605456897")

    @JvmField
    val CCMD_ROLE: Property<String> = newProperty("role.ccmd", "713929371290959904")

    @JvmField
    val MF_ROLE: Property<String> = newProperty("role.mf", "713929424269082684")

    @JvmField
    val MATT_ROLE: Property<String> = newProperty("role.matt", "713929561603047466")

    @JvmField
    val SETTINGS_MESSAGE: Property<String> = newProperty("message.settings", "0")

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
    val LEAK_WORDS: Property<MutableList<String>> = newListProperty("leak-words")

    @JvmField
    val BLACK_LISTED_CHANNELS: Property<MutableList<String>> = newListProperty("black-listed-channels")

}
