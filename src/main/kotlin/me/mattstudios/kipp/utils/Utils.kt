package me.mattstudios.kipp.utils

import me.mattstudios.kipp.data.Cache
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import java.awt.Color


object Utils {

    /**
     * Gets the color from HEX code
     */
    fun hexToRgb(colorStr: String): Color {
        return Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16))
    }

    /**
     * Sets all the default roles to the player
     */
    fun setRoles(cache: Cache, guild: Guild, member: Member) {
        val memberRole = cache.memberRole
        if (memberRole != null && !member.roles.contains(memberRole)) {
            guild.addRoleToMember(member, memberRole).queue()
        }

        val pluginsRole = cache.pluginsRole
        if (pluginsRole != null && !member.roles.contains(pluginsRole)) {
            guild.addRoleToMember(member, pluginsRole).queue()
        }

        val pingsRole = cache.pingsRole
        if (pingsRole != null && !member.roles.contains(pingsRole)) {
            guild.addRoleToMember(member, pingsRole).queue()
        }
    }

}