package me.mattstudios.kipp.listeners

import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.kipp.utils.Utils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Invite
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent
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
class JoinListener(private val jda: JDA, private val config: Config) : ListenerAdapter() {

    private var joinChannel: TextChannel? = null
    private var invites = mutableListOf<Invite>()
    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

    override fun onGuildReady(event: GuildReadyEvent) {
        val guild = event.guild
        joinChannel = guild.getTextChannelById(config[Setting.JOIN_CHANNEL])
        invites = guild.retrieveInvites().complete()
    }

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        val channel = joinChannel ?: return

        val member = event.member
        val user = event.user
        val guild = event.guild

        val invite = getInvite(guild)
        val today = LocalDateTime.now()

        val role = guild.getRoleById(config[Setting.DEFAULT_ROLE])
        println(role == null)
        if (role != null) {
             guild.addRoleToMember(member, role).queue()
        }

        val message = EmbedBuilder().setTitle("Member joined!")
                .setThumbnail(member.user.avatarUrl)
                .setColor(Utils.hexToRgb("#72d689"))
                .addField(user.asTag, member.asMention, false)
                .addField("Invite:", "`${invite?.code}` by `${invite?.inviter?.asTag}`", false)
                .addField("Account age:", abs(ChronoUnit.DAYS.between(today, invite?.timeCreated)).toString(), false)
                .setFooter(today.format(formatter))
                .build()

        channel.sendMessage(message).queue()
    }

    override fun onGuildMemberRemove(event: GuildMemberRemoveEvent) {
        println("left")
    }

    /**
     * Updates the cached invites list
     */
    override fun onGuildInviteCreate(event: GuildInviteCreateEvent) {
        invites = event.guild.retrieveInvites().complete()
    }

    /**
     * Gets the invite used
     */
    private fun getInvite(guild: Guild): Invite? {
        val newInvites = guild.retrieveInvites().complete()
        for (invite in newInvites) {
            val otherInvite = invites.find { it.code == invite.code }

            if (otherInvite == null) {
                invites = newInvites
                return null
            }

            if (invite.uses > otherInvite.uses) {
                invites = newInvites
                return invite
            }
        }

        invites = newInvites
        return null
    }

}