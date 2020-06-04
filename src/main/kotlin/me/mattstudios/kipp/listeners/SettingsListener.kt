package me.mattstudios.kipp.listeners

import me.mattstudios.kipp.data.Cache
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import sun.security.krb5.Config

/**
 * @author Matt
 */
class SettingsListener(private val config: Config, private val cache: Cache) : ListenerAdapter() {

    override fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent) {
        super.onGuildMessageReactionAdd(event)
    }

    override fun onGuildMessageReactionRemove(event: GuildMessageReactionRemoveEvent) {
        super.onGuildMessageReactionRemove(event)
    }

}