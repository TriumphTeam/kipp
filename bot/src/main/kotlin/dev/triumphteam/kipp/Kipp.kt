package dev.triumphteam.kipp

import dev.triumphteam.core.feature.install
import dev.triumphteam.core.jda.JdaApplication
import dev.triumphteam.core.jda.commands.PrefixedCommands
import dev.triumphteam.core.jda.commands.SlashCommands
import dev.triumphteam.core.jda.commands.commands
import dev.triumphteam.core.jda.commands.listeners
import dev.triumphteam.core.jda.commands.prefixedCommands
import dev.triumphteam.kipp.button.Buttons
import dev.triumphteam.kipp.button.buttons
import dev.triumphteam.kipp.buttons.GetRoleButtons
import dev.triumphteam.kipp.commands.prefixed.IntroductionSetup
import dev.triumphteam.kipp.commands.slash.DocCommand
import dev.triumphteam.kipp.commands.slash.DocsCommand
import dev.triumphteam.kipp.config.Config
import dev.triumphteam.kipp.database.Database
import dev.triumphteam.kipp.func.EVERYDAY
import dev.triumphteam.kipp.func.registerDefaults
import dev.triumphteam.kipp.invites.InvitesHandler
import dev.triumphteam.kipp.listener.MemberListener
import dev.triumphteam.kipp.listener.MessageLogListener
import dev.triumphteam.kipp.tasks.deleteOldMessages
import dev.triumphteam.kipp.tasks.updatePresence
import dev.triumphteam.scheduler.Scheduler
import dev.triumphteam.scheduler.runTaskEvery
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.GatewayIntent
import java.time.LocalTime
import kotlin.time.Duration.Companion.minutes

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
        install(PrefixedCommands)
        install(InvitesHandler)
        install(Buttons)
    }

    override fun onReady() {
        listeners {
            register(
                MessageLogListener(kipp),
                MemberListener(kipp),
            )
        }

        prefixedCommands {
            registerDefaults()

            register(
                IntroductionSetup(kipp),
            )
        }

        runTaskEvery(EVERYDAY, LocalTime.MIDNIGHT, ::deleteOldMessages)
        runTaskEvery(2.minutes, task = ::updatePresence)
    }

    override fun onGuildReady(guild: Guild) {
        commands {
            register(
                guild,
                //AboutCommand(jda),
                DocCommand(),
                DocsCommand(),
            )
        }

        buttons {
            register(
                GetRoleButtons(kipp, guild),
            )
        }
    }
}
