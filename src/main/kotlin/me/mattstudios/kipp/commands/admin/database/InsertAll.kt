package me.mattstudios.kipp.commands.admin.database

import me.mattstudios.kipp.data.Database
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.annotations.Requirement
import me.mattstudios.mfjda.base.CommandBase

/**
 * @author Matt
 */
@Prefix("!")
@Command("insertall")
class InsertAll(private val database: Database) : CommandBase() {

    @Default
    @Requirement("#admin-up")
    fun insertAll() {
        database.insertAll(message.guild)
        message.channel.sendMessage("Inserted all successfully!").queue()
    }

}