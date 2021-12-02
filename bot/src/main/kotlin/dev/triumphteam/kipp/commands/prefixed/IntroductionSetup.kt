package dev.triumphteam.kipp.commands.prefixed

import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.Command
import dev.triumphteam.cmd.core.annotation.Default
import dev.triumphteam.cmd.core.annotation.Optional
import dev.triumphteam.cmd.prefixed.annotation.Prefix
import dev.triumphteam.cmd.prefixed.sender.PrefixedSender
import dev.triumphteam.core.feature.feature
import dev.triumphteam.kipp.Kipp
import dev.triumphteam.kipp.config.Config
import dev.triumphteam.kipp.config.Settings
import dev.triumphteam.kipp.func.embed
import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.entities.Emote
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.interactions.components.Button
import net.dv8tion.jda.api.interactions.components.Component

@Prefix("!")
@Command("introduction")
class IntroductionSetup(kipp: Kipp) : BaseCommand() {

    private val config = kipp.feature(Config)

    @Default
    fun PrefixedSender.default(channel: TextChannel, @Optional messageId: Long?) {
        val emotes = config[Settings.EMOTES]
        val libEmote = guild.getEmoteById(emotes.libs) ?: return
        val extraEmote = guild.getEmoteById(emotes.libs) ?: return
        val mattEmote = guild.getEmoteById(emotes.matt) ?: return

        if (messageId == null) {
            channel.sendMessageEmbeds(
                createMessage(libEmote, extraEmote, mattEmote)
            ).setActionRow(
                createActionRow(libEmote, extraEmote, mattEmote)
            ).queue()
            return
        }

        channel.retrieveMessageById(messageId).queue {
            it.editMessageEmbeds(
                createMessage(libEmote, extraEmote, mattEmote)
            ).setActionRow(
                createActionRow(libEmote, extraEmote, mattEmote)
            ).queue()
        }
    }

    private fun createMessage(
        libEmote: Emote,
        extraEmote: Emote,
        mattEmote: Emote,
    ): MessageEmbed {
        return embed {
            description("Hello peoples, this will be a message.")
            field("Opt-in", "Placeholder shit.")
            field("", "${libEmote.asMention} | **Plugins** - Soon:tm:.")
            field("", "${extraEmote.asMention} | **Libraries** - Get pinged for library updates.")
            field("", "${extraEmote.asMention} | **Extra** - Get pinged for other projects like our core or gradle plugin.")
            field("", "${mattEmote.asMention} | **Matt** - Get access to Matt's chill corner")
        }
    }

    private fun createActionRow(
        libEmote: Emote,
        extraEmote: Emote,
        mattEmote: Emote,
    ): List<Component> {
        return listOf(
            Button.secondary("get-role-lib", Emoji.fromEmote(libEmote)),
            Button.secondary("get-role-extra", Emoji.fromEmote(extraEmote)),
            Button.secondary("get-role-matt", Emoji.fromEmote(mattEmote))
        )
    }

}