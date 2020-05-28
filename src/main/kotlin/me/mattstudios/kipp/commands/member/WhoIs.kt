package me.mattstudios.kipp.commands.member

import me.mattstudios.kipp.data.Database
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Optional
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.base.CommandBase
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Member
import java.time.format.DateTimeFormatter

/**
 * @author Matt
 */
@Prefix("!")
@Command("whois")
class WhoIs(private val database: Database) : CommandBase() {

    private val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    @Default
    fun whoIs(@Optional whoMember: Member?) {
        val commandUser = message.member ?: return
        val member = whoMember ?: message.member ?: return
        val user = member.user

        val embed = Embed(message.author).title(user.asTag)
                .thumbnail(user.avatarUrl ?: user.defaultAvatarUrl)
                .field("Name", user.name, true)
                .field("Status", getStatus(member.onlineStatus), true)
                .field("ID", "`${user.id}`", true)
                .field("Created", member.timeCreated.format(formatter), true)
                .field("Joined", member.timeJoined.format(formatter), true)
                .field("Linked", "Nothing for now", true)

        val adminRole = message.guild.getRolesByName("admin", true).firstOrNull()
        val invite = database.getInvite(member.idLong)
        if (adminRole != null && commandUser.roles.any { it.position >= adminRole.position }) {
            if (invite != null) {
                embed.empty()
                embed.field("Invite", "`${invite.invite}`", true)
                embed.field("Inviter", invite.inviter, true)
            }
        }

        message.channel.sendMessage(embed.build()).queue()
    }

    /**
     * Gets the message equivalent for the current status
     */
    private fun getStatus(onlineStatus: OnlineStatus): String {
        return when (onlineStatus) {
            OnlineStatus.ONLINE -> "Online"
            OnlineStatus.DO_NOT_DISTURB -> "DND"
            OnlineStatus.IDLE -> "Idle"
            else -> "Offline"
        }
    }

}