package me.mattstudios.kipp.commands.admin.defaults

import me.mattstudios.kipp.data.Cache
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
@Command("setjoinchannel")
class SetJoinChannel(private val config: Config, private val cache: Cache) : CommandBase() {

    @Default
    @Delete
    @Requirement("#admin-up")
    fun setJoinChannel() {
        config[Setting.JOIN_LOG_CHANNEL] = message.channel.id
        cache.joinChannel = message.textChannel
    }

}