package me.mattstudios.kipp.listeners

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

/**
 * @author Matt
 */
class StatusListener : ListenerAdapter() {

    /**
     * Sets the presence to the member count when the guild starts
     */
    override fun onGuildReady(event: GuildReadyEvent) {
        updatePresence(event.jda, event.guild.memberCount)
    }

    /**
     * Updates the member count when a player joins the server
     */
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        updatePresence(event.jda, event.guild.memberCount)
    }

    /**
     * Updates the member count when a player leaves the server
     */
    override fun onGuildMemberRemove(event: GuildMemberRemoveEvent) {
        updatePresence(event.jda, event.guild.memberCount)
    }

    /**
     * Updates the presence to the passed member count
     */
    private fun updatePresence(jda: JDA, members: Int) = jda.presence.setPresence(Activity.watching("$members members"), false)

}