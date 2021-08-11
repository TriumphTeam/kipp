package dev.triumphteam.kipp

import dev.triumphteam.bukkit.feature.install
import dev.triumphteam.jda.JdaApplication
import dev.triumphteam.jda.jda
import dev.triumphteam.kipp.config.Config
import dev.triumphteam.kipp.database.Database
import dev.triumphteam.kipp.event.listen
import dev.triumphteam.kipp.func.tokenFromFlag
import dev.triumphteam.kipp.listener.logMessages
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import java.io.File

fun main(args: Array<String>) {
    val token = tokenFromFlag(args)

    jda(
        module = JdaApplication::module,
        token = token,
        intents = listOf(
            GatewayIntent.GUILD_EMOJIS,
            GatewayIntent.GUILD_BANS,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MESSAGE_REACTIONS,
            GatewayIntent.GUILD_MESSAGE_TYPING,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_INVITES,
            GatewayIntent.GUILD_PRESENCES,
            GatewayIntent.GUILD_VOICE_STATES
        ),
        applicationFolder = File("data"),
    ) {
        disableCache(CacheFlag.ACTIVITY)
    }
}

fun JdaApplication.module() {
    install(Config)
    install(Database)

    listen(JdaApplication::logMessages)
}