package dev.triumphteam.kipp.buttons

import dev.triumphteam.core.feature.feature
import dev.triumphteam.kipp.Kipp
import dev.triumphteam.kipp.button.BaseButton
import dev.triumphteam.kipp.button.Button
import dev.triumphteam.kipp.button.Defer
import dev.triumphteam.kipp.config.Config
import dev.triumphteam.kipp.config.KippColor
import dev.triumphteam.kipp.config.Settings
import dev.triumphteam.kipp.func.embed
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent

@Button("get-role")
@Defer(ephemeral = true)
class GetRoleButtons(kipp: Kipp, guild: Guild) : BaseButton() {

    private val config = kipp.feature(Config)

    private val roles = config[Settings.ROLES]

    private val separator = guild.getRoleById(roles.separator)
    //private val plugins = guild.getRoleById(roles.separator)
    private val libraries = guild.getRoleById(roles.libs)
    private val extra = guild.getRoleById(roles.extra)
    private val matt = guild.getRoleById(roles.matt)

    private val roleList = setOf(libraries, extra, matt)

    @Button("matt")
    fun ButtonClickEvent.getMattRole() {
        handleRole(
            matt,
            addText = "Welcome to Matt's chill corner!",
            removeText = "You're no longer part of Matt's chill corner!"
        )
    }

    @Button("lib")
    fun ButtonClickEvent.getLibRole() {
        handleRole(libraries, "library")
    }

    @Button("extra")
    fun ButtonClickEvent.getExtraRole() {
        handleRole(extra, "extra")
    }

    private fun ButtonClickEvent.handleRole(
        role: Role?,
        updateType: String = "",
        addText: String = "",
        removeText: String = ""
    ) {
        val guild = guild ?: return
        val member = member ?: return
        val useRole = role ?: return

        if (useRole in member.roles) {
            guild.handleSeparator(member, true)
            guild.removeRoleFromMember(member, useRole).queue()
            hook.editOriginalEmbeds(createRemovedEmbed(updateType, removeText)).queue()
            return
        }

        guild.handleSeparator(member)
        guild.addRoleToMember(member, useRole).queue()
        hook.editOriginalEmbeds(createAddedEmbed(updateType, addText)).queue()
    }

    private fun Guild.handleSeparator(member: Member, remove: Boolean = false) {
        val roles = member.roles

        if (remove) {
            if (roles.filter { it in roleList }.size == 1) separator?.let {
                if (it !in roles) return@let
                removeRoleFromMember(member, it).queue()
            }
            return
        }

        if (roles.none { it in roleList }) separator?.let {
            if (it in roles) return@let
            addRoleToMember(member, it).queue()
        }

    }

    private fun createAddedEmbed(updateType: String, text: String = ""): MessageEmbed {
        return embed {
            val message = text.ifEmpty { "You'll now receive **${updateType}** updates!" }
            description(message)
            color(KippColor.SUCCESS)
        }
    }

    private fun createRemovedEmbed(updateType: String, text: String = ""): MessageEmbed {
        return embed {
            val message = text.ifEmpty { "You'll no longer receive **${updateType}** updates!" }
            description(message)
            color(KippColor.FAIL)
        }
    }

}