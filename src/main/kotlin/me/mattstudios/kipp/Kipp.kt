package me.mattstudios.kipp

import me.mattstudios.kipp.commands.admin.Purge
import me.mattstudios.kipp.commands.admin.database.UpdateDb
import me.mattstudios.kipp.commands.admin.defaults.SetDefaultRole
import me.mattstudios.kipp.commands.admin.defaults.SetJoinChannel
import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.data.Database
import me.mattstudios.kipp.listeners.JoinListener
import me.mattstudios.kipp.listeners.StatusListener
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.mfjda.base.CommandManager
import me.mattstudios.mfjda.base.components.TypeResult
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 * @author Matt
 */
class Kipp {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(Kipp::class.java)
    }

    // Creates the config and the logger
    private val config = Config()

    // Database handler
    private val database = Database(config)

    // Cache that holds important values
    private val cache = Cache(config)

    // Creates JDA object and starts the bot
    private val jda = JDABuilder
            .create(config[Setting.TOKEN],
                         listOf(
                                 GatewayIntent.GUILD_EMOJIS,
                                 GatewayIntent.GUILD_BANS,
                                 GatewayIntent.GUILD_MESSAGES,
                                 GatewayIntent.GUILD_MESSAGE_REACTIONS,
                                 GatewayIntent.GUILD_MESSAGE_TYPING,
                                 GatewayIntent.GUILD_MEMBERS,
                                 GatewayIntent.GUILD_INVITES
                         ))
            .disableCache(listOf(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.CLIENT_STATUS))
            .addEventListeners(cache, StatusListener())
            .build()

    // The command manager
    private val commandManager = CommandManager(jda)

    /**
     * Calls all the registers
     */
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

                UpdateDb(database),

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
                JoinListener(config, cache, database)
        ).forEach { jda.addEventListener(it) }
    }

}