package me.mattstudios.kipp.manager

import me.mattstudios.kipp.data.Database
import me.mattstudios.kipp.json.JsonEmbed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.mfjda.base.CommandBuilder
import me.mattstudios.mfjda.base.CommandManager

/**
 * @author Matt
 */
class FaqManager(
        private val commandManager: CommandManager,
        private val database: Database
) {

    private val commands = mutableListOf<String>()

    /**
     * Registers all the faq commands from the config
     */
    fun registerAll() {
        database.getFaqs().forEach { registerCommand(it) }
    }

    /**
     * Creates a new faq
     */
    fun createFaq(jsonEmbed: JsonEmbed) {
        registerCommand(jsonEmbed)
    }

    /**
     * Deletes the given faq command
     */
    fun delete(command: String): Boolean {
        if (command !in commands) return false

        database.deleteFaq(command)

        commands.remove(command)
        commandManager.unregister("?", command)
        return true
    }

    /**
     * Gets the list of faq commands
     */
    fun getFaqs(): List<String> {
        return commands
    }

    /**
     * Registers the faq command
     */
    private fun registerCommand(jsonEmbed: JsonEmbed) {
        val command = jsonEmbed.command ?: return
        commands.add(command)

        val faqCommand = CommandBuilder()
                .setPrefix("?")
                .setCommand(command)
                .setArgumentsLimit(0)
                .setExecutor { _, message ->
                    message.textChannel.queueMessage(jsonEmbed.toEmbed(message.author))
                }
                .build()

        commandManager.register(faqCommand)

    }

}
