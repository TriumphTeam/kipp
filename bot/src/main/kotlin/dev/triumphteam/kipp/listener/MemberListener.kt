package dev.triumphteam.kipp.listener

import dev.triumphteam.core.feature.feature
import dev.triumphteam.kipp.Kipp
import dev.triumphteam.kipp.config.Config
import dev.triumphteam.kipp.config.KippColor
import dev.triumphteam.kipp.config.Settings
import dev.triumphteam.kipp.func.embed
import dev.triumphteam.kipp.func.queueMessage
import dev.triumphteam.kipp.invites.InvitesHandler
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
    private val invitesHandler = kipp.feature(InvitesHandler)

    override fun onGuildReady(event: GuildReadyEvent): Unit = with(event) {
        //invites = guild.retrieveInvites().complete()
    }

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent): Unit = with(event) {
        val invite = invitesHandler.getLastInvite(guild)

        guild.getRoleById(config[Settings.ROLES].member)?.let {
            guild.addRoleToMember(member, it).queue()
        }

        val message = embed {
            title("Member joined!")
            thumbnail(user)
            color(KippColor.SUCCESS)

            fields {
                field(user.asTag, member.asMention)
                field("Invite:", "`${invite?.code}` by `${invite?.inviter?.asTag}`")
                field("Account age:", abs(ChronoUnit.DAYS.between(LocalDateTime.now(), user.timeCreated)).toString())
            }

            footer("ID: ${user.id}")
        }

        guild.getTextChannelById(config[Settings.CHANNELS].joinLeave)?.queueMessage(message)
    }

    override fun onGuildMemberRemove(event: GuildMemberRemoveEvent): Unit = with(event) {
        val message = embed {
            title("Member left!")
            thumbnail(user.avatarUrl ?: user.defaultAvatarUrl)
            color(KippColor.FAIL)

            fields {
                field(user.asTag, user.asMention)
            }

            footer(LocalDateTime.now().format(formatter))
        }

        guild.getTextChannelById(config[Settings.CHANNELS].joinLeave)?.queueMessage(message)
    }

}