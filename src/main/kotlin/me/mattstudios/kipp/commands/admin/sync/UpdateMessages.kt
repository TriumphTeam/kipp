package me.mattstudios.kipp.commands.admin.sync

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
@Command("updatemessages")
class UpdateMessages(private val database: Database) : CommandBase() {

    @Default
    @Requirement("#admin-up")
    fun insertAll() {
        GlobalScope.launch { database.insertAllMessages(message.guild, message.textChannel) }
        message.textChannel.queueMessage(Embed(message.author).field("Updating database messages", "Working on it!").build())
    }

}