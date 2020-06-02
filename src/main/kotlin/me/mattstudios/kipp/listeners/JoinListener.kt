package me.mattstudios.kipp.listeners

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.data.Database
import me.mattstudios.kipp.utils.Color
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.kipp.utils.Utils.formatter
import me.mattstudios.kipp.utils.Utils.setRoles
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Invite
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs

/**
 * @author Matt
 */
class JoinListener(
        private val cache: Cache,
        private val database: Database
) : ListenerAdapter() {

    /**
     * Handles the member join event
     */
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        val channel = cache.joinChannel ?: return

        val member = event.member
        val user = event.user
        val guild = event.guild

        val invite = getInvite(guild)
        val today = LocalDateTime.now()

        setRoles(cache, guild, member)

        val message = Embed().title("Member joined!")
                .thumbnail(user.avatarUrl ?: user.defaultAvatarUrl)
                .color(Color.SUCCESS)
                .field(user.asTag, member.asMention)
                .field("Invite:", "`${invite?.code}` by `${invite?.inviter?.asTag}`")
                .field("Account age:", abs(ChronoUnit.DAYS.between(today, user.timeCreated)).toString())
                .footer("ID: ${user.id}")
                .build()

        channel.queueMessage(message)

        GlobalScope.launch { database.insertMember(member, invite) }

    }

    /**
     * Handles the member leave event
     */
    override fun onGuildMemberRemove(event: GuildMemberRemoveEvent) {
        val channel = cache.joinChannel ?: return

        val user = event.user

        val message = Embed().title("Member left!")
                .thumbnail(user.avatarUrl ?: user.defaultAvatarUrl)
                .color(Color.FAIL)
                .field(user.asTag, user.asMention)
                .footer(LocalDateTime.now().format(formatter))
                .build()

        channel.queueMessage(message)
    }

    /**
     * Gets the invite used
     */
    private fun getInvite(guild: Guild): Invite? {
        val newInvites = guild.retrieveInvites().complete()

        val invite = newInvites.find { invite ->
            val foundInvite = cache.invites.find { it.code == invite.code } ?: return@find false
            return@find invite.uses > foundInvite.uses
        }

        cache.invites = newInvites

        return invite
    }

}