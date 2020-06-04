package me.mattstudios.kipp.commands.admin.defaults

import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.annotations.Requirement
import me.mattstudios.mfjda.base.CommandBase

/**
 * @author Matt
 */
@Prefix("!")
@Command("setrole")
class SetDefaultRole(private val config: Config, private val cache: Cache) : CommandBase() {

    @Default
    @Requirement("#admin-up")
    fun setDefaultRoles(role: String, roleId: String) {
        val guild = message.guild

        when (role.toLowerCase()) {
            "member" -> {
                config[Setting.MEMBER_ROLE] = roleId
                cache.memberRole = guild.getRoleById(roleId)
            }

            "plugins" -> {
                config[Setting.PLUGINS_ROLE] = roleId
                cache.pluginsRole = guild.getRoleById(roleId)
            }

            "subscriptions" -> {
                config[Setting.SUBSCRIPTIONS_ROLE] = roleId
                cache.subscriptionsRole = guild.getRoleById(roleId)
            }
        }
    }

}