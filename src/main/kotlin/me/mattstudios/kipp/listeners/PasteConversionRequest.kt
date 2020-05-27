package me.mattstudios.kipp.listeners

import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.Utils.createPaste
import me.mattstudios.kipp.utils.Utils.plural
import me.mattstudios.kipp.utils.Utils.readContent
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.net.URL
import java.util.regex.Pattern

/**
 * @author Matt
 */
class PasteConversionRequest(private val jda: JDA) : ListenerAdapter() {

    // Pattern to identify URLs in the message
    private val urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
            + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
            + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL)

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

        val links = extractLinks(message.contentRaw)
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

    /**
     * Gets all the links in a message
     */
    private fun extractLinks(message: String): List<URL> {
        val links = mutableListOf<URL>()

        val matcher = urlPattern.matcher(message)
        while (matcher.find()) {
            val matchStart = matcher.start(1)
            val matchEnd = matcher.end()

            links.add(URL(message.substring(matchStart, matchEnd)))
        }

        return links
    }

}