package me.mattstudios.kipp.commands.member

import me.mattstudios.kipp.manager.TodoManager
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.base.CommandBase

/**
 * @author Matt
 */
@Prefix("!")
@Command("todo")
class Todo(private val todoManager: TodoManager) : CommandBase() {

    @Default
    fun todos() {
        val embed = Embed(message.author).field("Matt has the following to-do:", "```\n${todoManager.getTodos().joinList()}\n```")
        message.channel.sendMessage(embed.build()).queue()
    }

    private fun List<String>.joinList(): String = if (isEmpty()) "The list is currently empty." else join()
    private fun List<String>.join(): String = joinToString("\n") { "${indexOf(it) + 1} - $it" }

}