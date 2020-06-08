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
    var botCmdsChannel: TextChannel? = null

    var memberRole: Role? = null
    var pluginsRole: Role? = null
    var subscriptionsRole: Role? = null
    var adminRole: Role? = null
    var mattRole: Role? = null
    var mfRole: Role? = null
    var ccmdRole: Role? = null

    var yesEmote: Emote? = null
    var noEmote: Emote? = null
    var importantEmote: Emote? = null
    var mattEmote: Emote? = null
    var mfEmote: Emote? = null
    var ccmdEmote: Emote? = null
    var clipboardEmote: Emote? = null

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
        botCmdsChannel = guild.getTextChannelById(config[Setting.BOT_CMDS_CHANNEL])

        invites = guild.retrieveInvites().complete()

        memberRole = guild.getRoleById(config[Setting.MEMBER_ROLE])
        pluginsRole = guild.getRoleById(config[Setting.PLUGINS_ROLE])
        subscriptionsRole = guild.getRoleById(config[Setting.SUBSCRIPTIONS_ROLE])
        adminRole = guild.getRoleById(config[Setting.ADMIN_ROLE])
        mattRole = guild.getRoleById(config[Setting.MATT_ROLE])
        mfRole = guild.getRoleById(config[Setting.MF_ROLE])
        ccmdRole = guild.getRoleById(config[Setting.CCMD_ROLE])

        yesEmote = guild.getEmotesByName("yesmark", true).firstOrNull()
        noEmote = guild.getEmotesByName("nomark", true).firstOrNull()
        importantEmote = guild.getEmotesByName("important", true).firstOrNull()
        mattEmote = guild.getEmotesByName("matt", true).firstOrNull()
        mfEmote = guild.getEmotesByName("mf", true).firstOrNull()
        ccmdEmote = guild.getEmotesByName("ccmd", true).firstOrNull()
        clipboardEmote = guild.getEmotesByName("paste", true).firstOrNull()
    }

    /**
     * Updates the cached invites list
     */
    override fun onGuildInviteCreate(event: GuildInviteCreateEvent) {
        invites = event.guild.retrieveInvites().complete()
    }

}