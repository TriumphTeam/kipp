package me.mattstudios.kipp.commands.admin

import me.mattstudios.kipp.manager.TodoManager
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.annotations.SubCommand
import me.mattstudios.mfjda.base.CommandBase

/**
 * @author Matt
 */
@Prefix("!")
@Command("todos")
class TodoManage(private val todoManager: TodoManager) : CommandBase() {

    @SubCommand("add")
    fun createTodo(args: Array<String>) {
        todoManager.create(args.joinToString(" "))
        val embed = Embed(message.author).field("To-do", "To-do added successfully!")
        message.textChannel.queueMessage(embed.build())
    }

    @SubCommand("done")
    fun removeTodo(index: Int) {
        val removeEmbed = Embed(message.author)

        if (todoManager.remove(index)) {
            removeEmbed.field("FAQ delete", "The to-do `$index` was deleted successfully!")
        } else {
            removeEmbed.field("FAQ delete", "The to-do `$index` does not exist or an error occurred while deleting it!")
        }

        message.textChannel.queueMessage(removeEmbed.build())
    }

}