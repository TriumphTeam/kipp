package me.mattstudios.kipp.commands.common

import me.mattstudios.kipp.manager.TodoManager
import me.mattstudios.kipp.utils.Color
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.annotations.SubCommand
import me.mattstudios.mfjda.base.CommandBase

/**
 * @author Matt
 */
@Prefix("!")
@Command("todo")
class Todo(private val todoManager: TodoManager) : CommandBase() {

    @Default
    fun todo() {
        val embed = Embed(message.author)
                .field(
                        "Matt has the following to-do:",
                        "```\n" +
                        todoManager.getTodos().join() +
                        "\n```"
                )
        message.textChannel.queueMessage(embed.build())
    }

    @SubCommand("add")
    fun createTodo(id: String, args: Array<String>) {
        if (id in todoManager.getTodos().keys) {
            message.textChannel.queueMessage(
                    Embed(message.author)
                            .color(Color.FAIL)
                            .field("TO-DO was not added!", "The ID already exists!")
                            .build()
            )
            return
        }

        todoManager.create(id, args.joinToString(" "))

        val embed = Embed(message.author)
                .color(Color.SUCCESS)
                .field("To-do", "To-do added successfully!")

        message.textChannel.queueMessage(embed.build())
    }

    @SubCommand("done")
    fun removeTodo(id: String) {
        val removeEmbed = Embed(message.author)

        if (id !in todoManager.getTodos().keys) {
            message.textChannel.queueMessage(
                    Embed(message.author)
                            .color(Color.FAIL)
                            .field("TO-DO was not removed!", "The ID doesn't exists!")
                            .build()
            )
            return
        }

        todoManager.remove(id)

        removeEmbed
                .color(Color.SUCCESS)
                .field("FAQ delete", "The TO-DO `$id` was deleted successfully!")

        message.textChannel.queueMessage(removeEmbed.build())
    }

    private fun Map<String, String>.join() = if (isEmpty()) "The list is currently empty." else this.map { "• ${it.key} - ${it.value}" }.joinToString("\n")

}