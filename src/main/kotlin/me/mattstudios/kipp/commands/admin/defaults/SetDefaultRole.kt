package me.mattstudios.kipp.commands.admin.defaults

import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Delete
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.annotations.Requirement
import me.mattstudios.mfjda.base.CommandBase

/**
 * @author Matt
 */
@Prefix("!")
@Command("setrole")
class SetDefaultRole(private val config: Config) : CommandBase() {

    @Default
    @Delete
    @Requirement("#admin-up")
    fun setJoinChannel(role: String, roleId: String) {
        when (role.toLowerCase()) {
            "member" -> config[Setting.MEMBER_ROLE] = roleId
            "plugins" -> config[Setting.PLUGINS_ROLE] = roleId
            "pings" -> config[Setting.PINGS_ROLE] = roleId
        }
    }

}