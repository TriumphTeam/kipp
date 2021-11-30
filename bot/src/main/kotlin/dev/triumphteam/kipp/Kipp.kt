package dev.triumphteam.kipp

import dev.triumphteam.core.feature.install
import dev.triumphteam.core.jda.JdaApplication
import dev.triumphteam.core.jda.commands.SlashCommands
import dev.triumphteam.core.jda.commands.commands
import dev.triumphteam.core.jda.commands.listeners
import dev.triumphteam.kipp.commands.AboutCommand
import dev.triumphteam.kipp.config.Config
import dev.triumphteam.kipp.database.Database
import dev.triumphteam.kipp.listener.MemberListener
import dev.triumphteam.kipp.listener.MessageLogListener
import dev.triumphteam.kipp.scheduler.MINUTES_TILL_MIDNIGHT
import dev.triumphteam.kipp.scheduler.Scheduler
import dev.triumphteam.kipp.scheduler.checkOldMessages
import dev.triumphteam.kipp.scheduler.repeatingTask
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.GatewayIntent
import kotlin.time.Duration.Companion.hours

private val INTENTS = listOf(
    GatewayIntent.GUILD_EMOJIS,
    GatewayIntent.GUILD_BANS,
    GatewayIntent.GUILD_MESSAGES,
    GatewayIntent.GUILD_MESSAGE_REACTIONS,
    GatewayIntent.GUILD_MESSAGE_TYPING,
    GatewayIntent.GUILD_MEMBERS,
    GatewayIntent.GUILD_INVITES,
    GatewayIntent.GUILD_PRESENCES,
    GatewayIntent.GUILD_VOICE_STATES,
)

class Kipp(token: String) : JdaApplication(token, INTENTS) {

    // For when using inside receiver functions without needing `this@Kipp`
    private val kipp = this

    override fun onStart() {
        install(Config)
        install(Database)
        install(Scheduler)
        install(SlashCommands)
    }

    override fun onReady() {
        listeners {
            register(
                MessageLogListener(kipp),
                MemberListener(kipp),
            )
        }

        repeatingTask(period = 24.hours, delay = MINUTES_TILL_MIDNIGHT, task = ::checkOldMessages)
    }

    override fun onGuildReady(guild: Guild) {
        commands {
            register(guild, AboutCommand(jda))
        }
    }
}
