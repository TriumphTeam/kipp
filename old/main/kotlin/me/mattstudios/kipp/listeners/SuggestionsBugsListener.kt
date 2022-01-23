package me.mattstudios.kipp.listeners

import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.utils.Embed
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.util.concurrent.TimeUnit


/**
 * @author Matt
 */
class SuggestionsBugsListener(private val cache: Cache) : ListenerAdapter() {

    private val linePattern = Regex("^\\[([^]]*)] (.*)")

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (event.author.isBot) return

        val message = event.message
        val channel = event.channel

        if (channel != cache.bugsChannel && channel != cache.suggestionsChannel) return

        val matchesPattern = message.contentRaw.isPattern(channel.name)

        val member = event.member ?: return
        val adminRole = cache.adminRole ?: return

        if (!matchesPattern) {
            if (member.roles.any { role -> role.position >= adminRole.position }) return

            message.delete().queueAfter(1, TimeUnit.SECONDS)

            val embed = Embed().field(
                    "Wrong pattern!",
                    "Your message doesn't follow the pattern!\n" +
                    "Please check the pinned message for more information!"
            )

            channel.sendMessage(embed.build()).queue {
                it.delete().queueAfter(20, TimeUnit.SECONDS)
            }

            return
        }

        if (channel == cache.suggestionsChannel) {
            val yesEmote = cache.yesEmote ?: return
            val noEmote = cache.noEmote ?: return

            message.addReaction(yesEmote).queue()
            message.addReaction(noEmote).queue()
        }

        if (channel == cache.bugsChannel) {
            val importantEmote = cache.importantEmote ?: return
            message.addReaction(importantEmote).queue()
        }

    }

    /**
     * Checks if the message pattern is correct
     */
    private fun String.isPattern(channel: String): Boolean {
        val message = this.split("\n").drop(1)
        if (message.size <= 1) return false

        for (i in 0..1) {
            val (first, _) = linePattern.matchEntire(message[i])?.destructured ?: return false
            if (i == 0 && first != "plugin") return false
            if (i == 1 && first != channel.trimEnd('s')) return false
        }

        return true
    }

}