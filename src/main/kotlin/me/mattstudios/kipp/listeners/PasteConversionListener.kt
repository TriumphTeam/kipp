package me.mattstudios.kipp.listeners

import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.Utils.createPaste
import me.mattstudios.kipp.utils.Utils.extractLinks
import me.mattstudios.kipp.utils.Utils.plural
import me.mattstudios.kipp.utils.Utils.readContent
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.net.URL

/**
 * @author Matt
 */
class PasteConversionListener(private val jda: JDA) : ListenerAdapter() {

    /**
     * Listens for the message reaction add event to check for the no paste bin emote
     */
    override fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent) {
        val emote = event.reactionEmote
        val member = event.member

        if (!emote.isEmote) return
        if (emote.name != "nopastebin") return

        val adminRole = jda.getRoleById(496353695605456897) ?: return
        if (member.roles.none { role -> role.position >= adminRole.position }) return

        val message = event.channel.retrieveMessageById(event.reaction.messageId).complete()
        message.clearReactions(emote.emote).queue()

        val links = message.contentRaw.extractLinks()
        if (links.isEmpty()) return

        val newPastes = mutableListOf<String>()

        for (link in links) {
            if ("pastebin.com" !in link.host) continue

            val url = URL(link, "/raw${link.path}")
            newPastes.add(createPaste(url.openStream().readContent()))
        }

        val pasteSize = newPastes.size

        if (pasteSize == 0) return

        val embed = Embed()
                .field("Paste".plural(pasteSize) + " converted!", newPastes.joinToString("\n"))

        message.channel.sendMessage(embed.build()).queue()
    }

}