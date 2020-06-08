package me.mattstudios.kipp.listeners

import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.scheduler.Scheduler
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.kipp.utils.Color
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.util.concurrent.TimeUnit

/**
 * @author Matt
 */
class SettingsListener(
        private val config: Config,
        private val cache: Cache,
        private val scheduler: Scheduler
) : ListenerAdapter() {

    private val added = mutableMapOf<String, Long>()
    private val removed = mutableMapOf<String, Long>()

    private var botChannel: TextChannel? = null

    init {
        scheduler.scheduleRepeatingTask(1, 1, TimeUnit.MINUTES) {
            with(added.iterator()) {
                forEach { if (System.currentTimeMillis() - it.value >= TimeUnit.SECONDS.toMillis(30)) remove() }
            }

            with(removed.iterator()) {
                forEach { if (System.currentTimeMillis() - it.value >= TimeUnit.SECONDS.toMillis(30)) remove() }
            }
        }

    }

    /**
     * Adds roles to the subscribe category
     */
    override fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent) {
        if (event.messageId != config[Setting.SETTINGS_MESSAGE]) return
        val member = event.member
        val user = event.user
        val guild = event.guild

        if (user.isBot) return

        event.reaction.removeReaction(event.user).queue()

        botChannel = event.channel

        when (event.reactionEmote.emote) {
            // When reacting with CitizensCMD emote
            cache.ccmdEmote -> {
                val ccmdRole = cache.ccmdRole ?: return
                guild.addOrRemoveMemberRole(member, ccmdRole, "CitizensCMD")
                member.addSubscriptionsRole(guild)
            }

            // When reacting with MF emote
            cache.mfEmote -> {
                val mfRole = cache.mfRole ?: return
                guild.addOrRemoveMemberRole(member, mfRole, "Matt's Framework")
                member.addSubscriptionsRole(guild)
            }

            // When reacting with Matt emote
            cache.mattEmote -> {
                val mattRole = cache.mattRole ?: return
                guild.addOrRemoveMemberRole(member, mattRole, "Matt's dev zone")
                member.addSubscriptionsRole(guild)
            }
        }

    }

    /**
     * Adds or removes role to member
     */
    private fun Guild.addOrRemoveMemberRole(member: Member, role: Role, ping: String) {
        val memberRoles = member.roles

        if (role in memberRoles && serialize(member, role) !in removed) {
            removeRoleFromMember(member, role).queue()
            removed[serialize(member, role)] = System.currentTimeMillis()

            botChannel?.queueMessage(member.asMention)
            botChannel?.queueMessage(Embed().color(Color.FAIL).field("Subscription", "You are no longer subscribing to `$ping`!").build())
            return
        }

        if (serialize(member, role) in added) return
        addRoleToMember(member, role).queue()
        added[serialize(member, role)] = System.currentTimeMillis()

        botChannel?.queueMessage(member.asMention)
        botChannel?.queueMessage(Embed().color(Color.SUCCESS).field("Subscription", "You are now subscribing to `$ping`!").build())
    }

    /**
     * Adds the ping role to the member
     */
    private fun Member.addSubscriptionsRole(guild: Guild) {
        val subscriptionRole = cache.subscriptionsRole ?: return
        if (subscriptionRole in roles) return
        guild.addRoleToMember(this, subscriptionRole).queue()
    }

    /**
     * Adds the ping role to the member
     */
    private fun Member.checkSubscriptionsRole(guild: Guild) {
        val subscriptionRole = cache.subscriptionsRole ?: return
        if (this.roles.any { it.position < subscriptionRole.position }) return
        guild.removeRoleFromMember(this, subscriptionRole).queue()
    }

    /**
     * Serializes the member/role for the map
     */
    private fun serialize(member: Member, role: Role) = "${member.asMention}|${role.name}"

}