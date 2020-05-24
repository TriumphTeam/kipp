package me.mattstudios.kipp.listeners

import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.data.Database
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.kipp.utils.Utils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Invite
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.abs

/**
 * @author Matt
 */
class JoinListener(
        private val config: Config,
        private val cache: Cache,
        private val database: Database
) : ListenerAdapter() {

    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

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

        val role = guild.getRoleById(config[Setting.MEMBER_ROLE])
        if (role != null) guild.addRoleToMember(member, role).queue()

        val message = EmbedBuilder().setTitle("Member joined!")
                .setThumbnail(user.avatarUrl)
                .setColor(Utils.hexToRgb("#72d689"))
                .addField(user.asTag, member.asMention, false)
                .addField("Invite:", "`${invite?.code}` by `${invite?.inviter?.asTag}`", false)
                .addField("Account age:", abs(ChronoUnit.DAYS.between(today, user.timeCreated)).toString(), false)
                .setFooter(today.format(formatter))
                .build()

        channel.sendMessage(message).queue()

        database.insertMember(member, invite)
    }

    /**
     * Handles the member leave event
     */
    override fun onGuildMemberRemove(event: GuildMemberRemoveEvent) {
        val channel = cache.joinChannel ?: return

        val user = event.user

        val message = EmbedBuilder().setTitle("Member left!")
                .setThumbnail(user.avatarUrl)
                .setColor(Utils.hexToRgb("#d67272"))
                .addField(user.asTag, user.asMention, false)
                .setFooter(LocalDateTime.now().format(formatter))
                .build()

        channel.sendMessage(message).queue()
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