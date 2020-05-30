package me.mattstudios.kipp.listeners

import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.settings.Config
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

/**
 * @author Matt
 */
class MessageLogListener(config: Config, cache: Cache) : ListenerAdapter() {

    override fun onGuildMessageDelete(event: GuildMessageDeleteEvent) {
        println("deleted")
    }

}