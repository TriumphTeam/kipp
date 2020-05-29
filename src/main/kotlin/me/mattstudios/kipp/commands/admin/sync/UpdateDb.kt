package me.mattstudios.kipp.commands.admin.sync

import me.mattstudios.kipp.data.Database
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.annotations.Requirement
import me.mattstudios.mfjda.base.CommandBase

/**
 * @author Matt
 */
@Prefix("!")
@Command("updatedb")
class UpdateDb(private val database: Database) : CommandBase() {

    @Default
    @Requirement("#admin-up")
    fun insertAll() {
        database.insertAll(message.guild, message.channel)
        message.textChannel.queueMessage(Embed().field("Updating database", "Working on it!").build())
    }

}