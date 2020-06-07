package me.mattstudios.kipp.settings

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.configurationdata.CommentsConfiguration
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer.newListProperty
import ch.jalu.configme.properties.PropertyInitializer.newProperty

object Setting : SettingsHolder {

    @Comment("The token that is needed to login into the bot")
    val TOKEN: Property<String> = newProperty("token", "none")

    val JOIN_LOG_CHANNEL: Property<String> = newProperty("channel.join-log", "0")

    val LEAK_LOG_CHANNEL: Property<String> = newProperty("channel.leak-log", "0")

    val MESSAGE_LOG_CHANNEL: Property<String> = newProperty("channel.message-log", "0")

    val REMINDER_CHANNEL: Property<String> = newProperty("channel.reminder", "0")

    val SUGGESTIONS_CHANNEL: Property<String> = newProperty("channel.suggestions", "0")

    val BUGS_CHANNEL: Property<String> = newProperty("channel.bugs", "0")

    val MEMBER_ROLE: Property<String> = newProperty("role.member", "0")

    val PLUGINS_ROLE: Property<String> = newProperty("role.plugins", "0")

    val SUBSCRIPTIONS_ROLE: Property<String> = newProperty("role.subscriptions", "0713927697679777802")

    val ADMIN_ROLE: Property<String> = newProperty("role.admin", "496353695605456897")

    val CCMD_ROLE: Property<String> = newProperty("role.ccmd", "713929371290959904")

    val MF_ROLE: Property<String> = newProperty("role.mf", "713929424269082684")

    val MATT_ROLE: Property<String> = newProperty("role.matt", "713929561603047466")

    val SETTINGS_MESSAGE: Property<String> = newProperty("message.settings", "0")

    val SQL_HOST: Property<String> = newProperty("database.host", "localhost")

    val SQL_USER: Property<String> = newProperty("database.user", "matt")

    val SQL_PASSWORD: Property<String> = newProperty("database.password", "bleh")

    val SQL_DATABASE: Property<String> = newProperty("database.database", "matt")

    val DIALOGFLOW_PROJECT: Property<String> = newProperty("dialogflow.project-id", "")

    @Comment("\n", "List with all the leak detection words")
    val LEAK_WORDS: Property<MutableList<String>> = newListProperty("leak-words")

    @Comment("\n", "List with the ids of channels that should be excluded from the message logging")
    val BLACK_LISTED_CHANNELS: Property<MutableList<String>> = newListProperty("black-listed-channels")

    override fun registerComments(conf: CommentsConfiguration) {
        conf.setComment("channel", "\n", "The default channels for various actions")
        conf.setComment("role", "\n", "The default roles for various actions")
        conf.setComment("message", "\n", "Ids for various messages")
        conf.setComment("database", "\n", "Database data")
        conf.setComment("dialogflow", "\n", "Dialowgflow related settings")
    }
}
