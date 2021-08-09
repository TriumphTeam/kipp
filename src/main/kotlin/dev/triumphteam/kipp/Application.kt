package dev.triumphteam.kipp

import dev.triumphteam.jda.JdaApplication
import dev.triumphteam.jda.jda
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import java.io.File


fun main(args: Array<String>) {
    val commandLine = DefaultParser().parse(
        Options().apply {
            addOption(Option.builder("t").hasArg().argName("token").required().build())
        },
        args
    )

    val token = commandLine.getOptionValue("t")

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

}