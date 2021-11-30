package dev.triumphteam.kipp.listener

import dev.triumphteam.core.feature.feature
import dev.triumphteam.kipp.Kipp
import dev.triumphteam.kipp.config.Config
import dev.triumphteam.kipp.config.KippColor
import dev.triumphteam.kipp.config.Settings
import dev.triumphteam.kipp.func.embed
import dev.triumphteam.kipp.func.queueMessage
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Invite
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.abs

class MemberListener(kipp: Kipp) : ListenerAdapter() {
    private val config = kipp.feature(Config)
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    private var invites = mutableListOf<Invite>()

    override fun onGuildReady(event: GuildReadyEvent): Unit = with(event) {
        //invites = guild.retrieveInvites().complete()
    }

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent): Unit = with(event) {
        val invite = guild.getLastInvite()

        guild.getRoleById(config[Settings.ROLES].member)?.let {
            guild.addRoleToMember(member, it).queue()
        }

        val message = embed {
            title("Member joined!")
            thumbnail(user)
            color(KippColor.SUCCESS)
            field(user.asTag, member.asMention)
            field("Invite:", "`${invite?.code}` by `${invite?.inviter?.asTag}`")
            field("Account age:", abs(ChronoUnit.DAYS.between(LocalDateTime.now(), user.timeCreated)).toString())
            footer("ID: ${user.id}")
        }

        guild.getTextChannelById(config[Settings.CHANNELS].joinLeave)?.queueMessage(message)
    }

    override fun onGuildMemberRemove(event: GuildMemberRemoveEvent): Unit = with(event) {
        val message = embed {
            title("Member left!")
            thumbnail(user.avatarUrl ?: user.defaultAvatarUrl)
            color(KippColor.FAIL)
            field(user.asTag, user.asMention)
            footer(LocalDateTime.now().format(formatter))
        }

        guild.getTextChannelById(config[Settings.CHANNELS].joinLeave)?.queueMessage(message)
    }

    /**
     * Gets the invite used
     */
    private fun Guild.getLastInvite(): Invite? {
        val newInvites = retrieveInvites().complete()

        val invite = newInvites.find { invite ->
            val foundInvite = invites.find { it.code == invite.code } ?: return@find false
            return@find invite.uses > foundInvite.uses
        }

        invites = newInvites
        return invite
    }

}