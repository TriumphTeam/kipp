package dev.triumphteam.kipp.commands.slash

import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.Command
import dev.triumphteam.cmd.core.annotation.Default
import dev.triumphteam.cmd.core.annotation.Description
import dev.triumphteam.cmd.slash.sender.SlashSender
import dev.triumphteam.kipp.func.embed
import net.dv8tion.jda.api.JDA
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

@Command("about")
@Description("Shows information about the bot")
class AboutCommand(private val jda: JDA) : BaseCommand() {

    private val startTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())

    @Default
    fun SlashSender.about() {
        val kipp = jda.selfUser
        val uptime = (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - startTime).seconds

        val embed = embed {
            thumbnail(kipp.avatarUrl ?: kipp.defaultAvatarUrl)
            description(
                """
                    Hi, I am **Kipp**!
                    I was written in [Kotlin](https://kotlinlang.org/) using [JDA](https://github.com/DV8FromTheWorld/JDA).
                    My source can be found [here](https://github.com/TriumphTeam/kipp)!
                """.trimIndent()
            )

            fields {
                field("Status:", "<:online:718229424948117536> Operational", true)
                field("Uptime:", uptime.toString(), true)
            }

            footer("Created by: Matt#7079")
        }

        reply(embed).queue()
    }
}