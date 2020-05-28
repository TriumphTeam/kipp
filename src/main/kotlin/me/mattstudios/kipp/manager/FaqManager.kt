package me.mattstudios.kipp.manager

import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.kipp.utils.Embed
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
    private val commands = mutableListOf<String>()

    /**
     * Registers all the faq commands from the config
     */
    fun registerAll() {
        for (faq in config[Setting.FAQ_COMMANDS]) {
            val (command, title, body) = regex.matchEntire(faq)?.destructured ?: continue
            registerCommand(command, title, body)
        }
    }

    /**
     * Creates a new faq
     */
    fun createFaq(command: String, title: String, body: String) {
        val faqs = mutableListOf<String>()
        faqs.addAll(config[Setting.FAQ_COMMANDS])
        faqs.add("[$command] $title $body")
        config[Setting.FAQ_COMMANDS] = faqs

        registerCommand(command, title, body)
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
    private fun registerCommand(command: String, title: String, body: String) {
        commands.add(command)
        val faqMessage = Embed().field(title.replace('_', ' '), body).build()
        val faqCommand = CommandBuilder()
                .setPrefix("?")
                .setCommand(command)
                .setArgumentsLimit(0)
                .setExecutor { _, message -> message.channel.sendMessage(faqMessage).queue() }
                .build()

        commandManager.register(faqCommand)
    }

}