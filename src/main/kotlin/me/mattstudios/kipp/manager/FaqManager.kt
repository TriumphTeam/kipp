package me.mattstudios.kipp.manager

import me.mattstudios.kipp.data.JsonEmbed
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.mfjda.base.CommandBuilder
import me.mattstudios.mfjda.base.CommandManager

/**
 * @author Matt
 */
class FaqManager(
        private val commandManager: CommandManager,
        private val config: Config
) {

    private val regex = Regex("\\[([^]]*)] (\\w+) (.*)")
    private val thumbnailRegex = Regex("\\(([^]]*)\\)")
    private val imageRegex = Regex("\\[([^]]*)]")
    private val commands = mutableListOf<String>()

    /**
     * Registers all the faq commands from the config
     */
    fun registerAll() {
        /*for (faq in config[Setting.FAQ_COMMANDS]) {
            val (command, title, body) = regex.matchEntire(faq)?.destructured ?: continue
            registerCommand(command, title, body)
        }*/
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

        val faqs = mutableListOf<String>()
        faqs.addAll(config[Setting.FAQ_COMMANDS])
        faqs.removeIf { "[$command]" in it }
        config[Setting.FAQ_COMMANDS] = faqs

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
        commands.add(jsonEmbed.command)

        val faqCommand = CommandBuilder()
                .setPrefix("?")
                .setCommand(jsonEmbed.command)
                .setArgumentsLimit(0)
                .setExecutor { _, message ->
                    message.textChannel.queueMessage(jsonEmbed.toEmbed(message.author))
                }
                .build()

        commandManager.register(faqCommand)

    }

    /**
     * Escapes the line, removes the link and adds \n for new lines
     */
    private fun String.escapeLine(): String = replace(thumbnailRegex, "")
            .replace(imageRegex, "")
            .replace("\\n", "\n")


}
