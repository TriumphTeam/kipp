package me.mattstudios.kipp.listeners

import me.mattstudios.kipp.settings.Config
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

/**
 * @author Matt
 */
class LeakListener(private val config: Config) : ListenerAdapter() {

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {



    }

}