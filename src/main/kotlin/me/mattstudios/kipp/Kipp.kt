package me.mattstudios.kipp

import me.mattstudios.kipp.commands.admin.FaqsManage
import me.mattstudios.kipp.commands.admin.Purge
import me.mattstudios.kipp.commands.admin.TodoManage
import me.mattstudios.kipp.commands.admin.defaults.SetDefaultRole
import me.mattstudios.kipp.commands.admin.defaults.SetJoinChannel
import me.mattstudios.kipp.commands.admin.defaults.SetLeakChannel
import me.mattstudios.kipp.commands.admin.defaults.SetMessagesChannel
import me.mattstudios.kipp.commands.admin.sync.SyncRoles
import me.mattstudios.kipp.commands.admin.sync.UpdateDb
import me.mattstudios.kipp.commands.admin.sync.UpdateMessages
import me.mattstudios.kipp.commands.member.Faq
import me.mattstudios.kipp.commands.member.Paste
import me.mattstudios.kipp.commands.member.Todo
import me.mattstudios.kipp.commands.member.WhoIs
import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.data.Database
import me.mattstudios.kipp.listeners.JoinListener
import me.mattstudios.kipp.listeners.KippListener
import me.mattstudios.kipp.listeners.MessageLogListener
import me.mattstudios.kipp.listeners.MessagePasteListener
import me.mattstudios.kipp.listeners.PasteConversionListener
import me.mattstudios.kipp.listeners.StatusListener
import me.mattstudios.kipp.manager.FaqManager
import me.mattstudios.kipp.manager.TodoManager
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.mfjda.base.CommandManager
import me.mattstudios.mfjda.base.components.TypeResult
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.TextChannel
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
                            GatewayIntent.GUILD_INVITES,
                            GatewayIntent.GUILD_PRESENCES
                    ))
            .disableCache(
                    listOf(
                            CacheFlag.ACTIVITY,
                            CacheFlag.VOICE_STATE
                    )
            )
            .addEventListeners(cache, StatusListener())
            .build()

    // The command manager
    private val commandManager = CommandManager(jda)

    private val faqManager = FaqManager(commandManager, config)
    private val todoManager = TodoManager(config)

    /**
     * Calls all the registers
     */
    init {
        registerListeners()
        registerParameters()
        registerRequirements()
        registerMessages()
        registerCommands()

        faqManager.registerAll()
    }

    /**
     * Registers the requirements for the bot commands
     */
    private fun registerRequirements() {
        commandManager.registerRequirement("#admin-up") { member ->
            val adminRole = cache.adminRole ?: return@registerRequirement false
            return@registerRequirement member.roles.any { role -> role.position >= adminRole.position }
        }
    }

    /**
     * Registers messages for the commands
     */
    private fun registerMessages() {
        commandManager.registerMessage("cmd.no.permission") {}
        // TODO this ones
        commandManager.registerMessage("cmd.no.exists") { channel -> channel.queueMessage("Command doesn't exist") }
        commandManager.registerMessage("cmd.wrong.usage") { channel -> channel.queueMessage("Wrong usage") }
    }

    /**
     * Registers all the commands for the bot
     */
    private fun registerCommands() {
        val commands = listOf(
                Purge(),
                FaqsManage(faqManager),
                TodoManage(todoManager),

                WhoIs(database),
                Paste(),
                Faq(faqManager),
                Todo(todoManager),

                UpdateDb(database),
                UpdateMessages(database),
                SyncRoles(cache),

                SetJoinChannel(config, cache),
                SetDefaultRole(config, cache),
                SetLeakChannel(config, cache),
                SetMessagesChannel(config, cache)
        )

        logger.info("Registering ${commands.size} commands..")

        commands.forEach(commandManager::register)
    }

    /**
     * Registers the requirements for the bot commands
     */
    private fun registerParameters() {
        commandManager.registerParameter(Int::class.java) { argument -> TypeResult(argument.toString().toIntOrNull(), argument) }
        commandManager.registerParameter(Role::class.java) { argument -> TypeResult(jda.getRoleById(argument.toString()), argument) }

        commandManager.registerParameter(Member::class.java) { argument ->
            if (argument == null) return@registerParameter TypeResult(argument)
            val numericArg = argument.toString().replace(("[^\\d]").toRegex(), "")
            val guild = jda.guilds.firstOrNull() ?: return@registerParameter TypeResult(argument)
            return@registerParameter TypeResult(guild.getMemberById(numericArg), argument)
        }

        commandManager.registerParameter(TextChannel::class.java) { argument ->
            if (argument == null) return@registerParameter TypeResult(argument)
            val numericArg = argument.toString().replace(("[^\\d]").toRegex(), "")
            val guild = jda.guilds.firstOrNull() ?: return@registerParameter TypeResult(argument)
            return@registerParameter TypeResult(guild.getTextChannelById(numericArg), argument)
        }

    }

    /**
     * Registers the listeners for the bot
     */
    private fun registerListeners() {
        val listeners = listOf(
                JoinListener(cache, database),
                PasteConversionListener(jda),
                MessagePasteListener(config, cache),
                MessageLogListener(config, cache, database),
                KippListener(cache, config)
        )

        logger.info("Registering ${listeners.size} listeners..")

        listeners.forEach { jda.addEventListener(it) }
    }

}