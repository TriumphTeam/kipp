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
    private val thumbnailRegex = Regex("\\(([^]]*)\\)")
    private val imageRegex = Regex("\\[([^]]*)]")
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
    private fun registerCommand(command: String, title: String, body: String) {
        commands.add(command)
        val image = imageRegex.find(body)?.destructured?.component1()
        val thumbnail = thumbnailRegex.find(body)?.destructured?.component1()
        val embed = Embed()

        if (image != null) embed.image(image)
        if (thumbnail != null) embed.thumbnail(thumbnail)

        val faqMessage = embed.field(title.replace('_', ' '), body.escapeLine()).build()
        val faqCommand = CommandBuilder()
                .setPrefix("?")
                .setCommand(command)
                .setArgumentsLimit(0)
                .setExecutor { _, message -> message.channel.sendMessage(faqMessage).queue() }
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
