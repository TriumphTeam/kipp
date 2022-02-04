package dev.triumphteam.kipp.commands.prefixed

import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.Command
import dev.triumphteam.cmd.core.annotation.Default
import dev.triumphteam.cmd.core.annotation.Optional
import dev.triumphteam.cmd.core.annotation.Requirement
import dev.triumphteam.cmd.prefixed.annotation.Prefix
import dev.triumphteam.cmd.prefixed.sender.PrefixedSender
import dev.triumphteam.core.feature.get
import dev.triumphteam.kipp.Kipp
import dev.triumphteam.kipp.config.Config
import dev.triumphteam.kipp.config.Settings
import dev.triumphteam.kipp.func.embed
import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.entities.Emote
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.interactions.components.ItemComponent
import net.dv8tion.jda.api.interactions.components.buttons.Button

@Prefix("!")
@Command("introduction")
class IntroductionSetupCommand(kipp: Kipp) : BaseCommand() {

    private val config = kipp[Config]

    @Default
    @Requirement("admin")
    fun PrefixedSender.default(channel: TextChannel, @Optional messageId: Long?) {
        val emotes = config[Settings.EMOTES]
        val libEmote = guild.getEmoteById(emotes.libs) ?: return
        val extraEmote = guild.getEmoteById(emotes.triumph) ?: return
        val mattEmote = guild.getEmoteById(emotes.matt) ?: return

        if (messageId == null) {
            channel.sendMessageEmbeds(createMessage(libEmote, extraEmote, mattEmote))
                .setActionRow(createActionRow(libEmote, extraEmote, mattEmote))
                .queue()
            return
        }

        channel.retrieveMessageById(messageId).queue {
            it.editMessageEmbeds(createMessage(libEmote, extraEmote, mattEmote))
                .setActionRow(createActionRow(libEmote, extraEmote, mattEmote))
                .queue()
        }
    }

    private fun createMessage(
        libEmote: Emote,
        extraEmote: Emote,
        mattEmote: Emote,
    ): MessageEmbed {
        return embed {
            title("Welcome to the server!")
            description(
                """
                    Please ensure you read the rules at <#717208301850460181>.
                    
                    Feel free to ask questions, stick around and chat, or interact with any of the buttons below to receive updates on some of our future projects.
                    Enjoy your stay!
                """.trimIndent()
            )

            fields {
                field(
                    "**Settings**",
                    """
                        Click on the buttons below to toggle which updates you want to receive or if you want to be part of a channel.
                        
                        🔷 | **Plugins** - Soon:tm:.
                          
                        ${libEmote.asMention} | **Libraries** - Get pinged for library updates.
                          
                        ${extraEmote.asMention} | **Extra** - Get pinged for other projects like our core or gradle plugin.
                          
                        ${mattEmote.asMention} | **Matt** - Get access to Matt's chill corner.
                    """.trimIndent()
                )
            }
        }
    }

    private fun createActionRow(
        libEmote: Emote,
        extraEmote: Emote,
        mattEmote: Emote,
    ): List<ItemComponent> {
        return listOf(
            Button.secondary("get-role-lib", Emoji.fromEmote(libEmote)),
            Button.secondary("get-role-extra", "➕"),
            Button.secondary("get-role-matt", Emoji.fromEmote(mattEmote))
        )
    }

}