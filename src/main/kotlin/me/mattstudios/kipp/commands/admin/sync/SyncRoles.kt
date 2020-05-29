package me.mattstudios.kipp.commands.admin.sync

import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.kipp.utils.Utils
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.annotations.Requirement
import me.mattstudios.mfjda.base.CommandBase
import java.util.concurrent.CompletableFuture

/**
 * @author Matt
 */
@Prefix("!")
@Command("syncroles")
class SyncRoles(private val cache: Cache) : CommandBase() {

    @Default
    @Requirement("#admin-up")
    fun insertAll() {
        val guild = message.guild

        CompletableFuture.runAsync {
            guild.members.forEach {
                Utils.setRoles(cache, guild, it)
            }
        }

        message.textChannel.queueMessage(Embed().field("Sync role", "All member roles have been updated!").build())
    }

}