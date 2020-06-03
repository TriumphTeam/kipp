package me.mattstudios.kipp.data

import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import net.dv8tion.jda.api.entities.Emote
import net.dv8tion.jda.api.entities.Invite
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

/**
 * @author Matt
 */
class Cache(private val config: Config) : ListenerAdapter() {

    var joinChannel: TextChannel? = null
    var leakChannel: TextChannel? = null
    var messageChannel: TextChannel? = null
    var reminderChannel: TextChannel? = null
    var suggestionsChannel: TextChannel? = null
    var bugsChannel: TextChannel? = null

    var memberRole: Role? = null
    var pluginsRole: Role? = null
    var pingsRole: Role? = null
    var adminRole: Role? = null

    var yesEmote: Emote? = null
    var noEmote: Emote? = null
    var importantEmote: Emote? = null

    var invites = mutableListOf<Invite>()

    /**
     * Caches the data needed
     */
    override fun onGuildReady(event: GuildReadyEvent) {
        val guild = event.guild
        joinChannel = guild.getTextChannelById(config[Setting.JOIN_LOG_CHANNEL])
        leakChannel = guild.getTextChannelById(config[Setting.LEAK_LOG_CHANNEL])
        messageChannel = guild.getTextChannelById(config[Setting.MESSAGE_LOG_CHANNEL])
        reminderChannel = guild.getTextChannelById(config[Setting.REMINDER_CHANNEL])
        suggestionsChannel = guild.getTextChannelById(config[Setting.SUGGESTIONS_CHANNEL])
        bugsChannel = guild.getTextChannelById(config[Setting.BUGS_CHANNEL])

        invites = guild.retrieveInvites().complete()

        memberRole = guild.getRoleById(config[Setting.MEMBER_ROLE])
        pluginsRole = guild.getRoleById(config[Setting.PLUGINS_ROLE])
        pingsRole = guild.getRoleById(config[Setting.PINGS_ROLE])
        adminRole = guild.getRoleById(config[Setting.ADMIN_ROLE])

        yesEmote = guild.getEmotesByName("yesmark", true).firstOrNull()
        noEmote = guild.getEmotesByName("nomark", true).firstOrNull()
        importantEmote = guild.getEmotesByName("important", true).firstOrNull()
    }

    /**
     * Updates the cached invites list
     */
    override fun onGuildInviteCreate(event: GuildInviteCreateEvent) {
        invites = event.guild.retrieveInvites().complete()
    }

}