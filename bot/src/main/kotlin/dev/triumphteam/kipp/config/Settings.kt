package dev.triumphteam.kipp.config

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Name
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property

object Settings : SettingsHolder {

    @Path("message-log-blacklisted-channels")
    val MESSAGE_LOG_BLACKLISTED_CHANNELS = Property.create(listOf())

    @Path("message-log-blacklisted-categories")
    val MESSAGE_LOG_BLACKLISTED_CATEGORIES = Property.create(listOf())

    @Path("channels")
    val CHANNELS = Property.create(ChannelsHolder())

    @Path("emojis")
    val EMOTES = Property.create(EmojiHolder())

    @Path("roles")
    val ROLES = Property.create(RolesHolder())

    @Path("leak-words")
    val LEAK_WORDS = Property.create(emptyList())

}

data class ChannelsHolder(
    var messages: String = "",
    var kipp: String = "",
    var leak: String = "",
    @Name("join-leave") var joinLeave: String = "",
    @Name("lib-updates") var libUpdates: String = "",
    @Name("plugin-updates") var pluginUpdates: String = "",
    @Name("extra-updates") var extraUpdates: String = "",
)

data class EmojiHolder(
    var paste: String = "",
    var matt: String = "",
    var libs: String = "",
    var triumph: String = "",
)

data class RolesHolder(
    var member: String = "",
    var matt: String = "",
    var libs: String = "",
    var extra: String = "",
    var separator: String = "",
)