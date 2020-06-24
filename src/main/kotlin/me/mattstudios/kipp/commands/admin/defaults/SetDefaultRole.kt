package me.mattstudios.kipp.commands.admin.defaults

import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.annotations.Requirement
import me.mattstudios.mfjda.base.CommandBase
import net.dv8tion.jda.api.entities.Role

/**
 * @author Matt
 */
@Prefix("!")
@Command("setrole")
class SetDefaultRole(private val config: Config, private val cache: Cache) : CommandBase() {

    @Default
    @Requirement("#admin-up")
    fun setDefaultRoles(roleName: String, role: Role) {

        when (roleName.toLowerCase()) {
            "member" -> {
                config[Setting.MEMBER_ROLE] = role.id
                cache.memberRole = role
            }

            "plugins" -> {
                config[Setting.PLUGINS_ROLE] = role.id
                cache.pluginsRole = role
            }

            "subscriptions" -> {
                config[Setting.SUBSCRIPTIONS_ROLE] = role.id
                cache.subscriptionsRole = role
            }
        }
    }

}