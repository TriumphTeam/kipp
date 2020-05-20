package me.mattstudios.kipp

import me.mattstudios.kipp.commands.admin.Purge
import me.mattstudios.kipp.commands.admin.defaults.SetDefaultRole
import me.mattstudios.kipp.commands.admin.defaults.SetJoinChannel
import me.mattstudios.kipp.listeners.JoinListener
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.mfjda.base.CommandManager
import me.mattstudios.mfjda.base.components.TypeResult
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import org.slf4j.LoggerFactory


/**
 * @author Matt
 */
class Kipp {

    private val config = Config()
    private val logger = LoggerFactory.getLogger(Kipp::class.java)

    private val jda = JDABuilder
            .createLight(config[Setting.TOKEN],
                    listOf(
                            GatewayIntent.GUILD_EMOJIS,
                            GatewayIntent.GUILD_BANS,
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.GUILD_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.GUILD_INVITES
                    ))
            .build()

    private val commandManager = CommandManager(jda)

    init {
        registerListeners()
        registerParameters()
        registerRequirements()
        registerMessages()
        registerCommands()
    }

    /**
     * Registers the requirements for the bot commands
     */
    private fun registerRequirements() {
        commandManager.registerRequirement("#admin-up") { member ->
            val adminRole = jda.getRoleById(496353695605456897) ?: return@registerRequirement false
            return@registerRequirement member.roles.any { role -> role.position >= adminRole.position }
        }
    }

    /**
     * Registers messages for the commands
     */
    private fun registerMessages() {
        commandManager.registerMessage("cmd.no.permission") {}
        // TODO this ones
        commandManager.registerMessage("cmd.no.exists") { channel -> channel.sendMessage("Command doesn't exist").queue() }
        commandManager.registerMessage("cmd.wrong.usage") { channel -> channel.sendMessage("Wrong usage").queue() }
    }

    /**
     * Registers all the commands for the bot
     */
    private fun registerCommands() {
        listOf(
                Purge(),

                SetJoinChannel(config),
                SetDefaultRole(config)
        ).forEach(commandManager::register)
    }

    /**
     * Registers the requirements for the bot commands
     */
    private fun registerParameters() {
        commandManager.registerParameter(Int::class.java) { argument ->
            return@registerParameter TypeResult(argument.toString().toIntOrNull(), argument)
        }
    }

    /**
     * Registers the listeners for the bot
     */
    private fun registerListeners() {
        listOf(
                JoinListener(jda, config)
        ).forEach { jda.addEventListener(it) }
    }

}