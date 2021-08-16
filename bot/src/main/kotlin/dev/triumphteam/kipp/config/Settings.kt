package dev.triumphteam.kipp.config

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property

object Settings : SettingsHolder {

    //val DATABASE = Property.create()
    @Path("message-log-blacklisted-channels")
    val MESSAGE_LOG_BLACKLISTED_CHANNELS = Property.create(listOf())

    @Path("message-log-blacklisted-categories")
    val MESSAGE_LOG_BLACKLISTED_CATEGORIES = Property.create(listOf())

    @Path("channels")
    val CHANNELS = Property.create(ChannelsHolder())

    @Path("emojis")
    val EMOJIS = Property.create(EmojiHolder())

    @Path("leak-words")
    val LEAK_WORDS = Property.create(emptyList())

}

data class ChannelsHolder(
    var messages: String = "",
    var kipp: String = "",
    var leak: String = "",
)

data class EmojiHolder(
    var paste: String = "",
)