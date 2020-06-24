package me.mattstudios.kipp.listeners

import me.mattstudios.kipp.Kipp
import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.utils.Color
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.kipp.utils.Utils.append
import me.mattstudios.kipp.utils.Utils.createPaste
import me.mattstudios.kipp.utils.Utils.extractLinks
import me.mattstudios.kipp.utils.Utils.plural
import me.mattstudios.kipp.utils.Utils.readContent
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

/**
 * @author Matt
 */
class PasteReactionListener(private val jda: JDA, private val cache: Cache) : ListenerAdapter() {

    private val extensions = listOf("yaml", "yml", "txt", "json", "class", "java", "kt")

    /**
     * Listens for the message reaction add event to check for the no paste bin emote
     */
    override fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent) {
        val emote = event.reactionEmote
        val member = event.member

        if (!emote.isEmote) return

        val pasteEmote = cache.clipboardEmote ?: return
        if (emote.emote != pasteEmote) return

        Kipp.logger.info("Converting paste!")

        val adminRole = jda.getRoleById(496353695605456897) ?: return
        if (member.roles.none { role -> role.position >= adminRole.position }) return

        val message = event.channel.retrieveMessageById(event.reaction.messageId).complete()
        message.clearReactions(emote.emote).queue()

        val links = message.contentRaw.extractLinks()
        val files = message.getFiles()
        if (links.isEmpty() && files.isEmpty()) return

        val newPastes = mutableListOf<String>()

        for (link in links) {
            if ("pastebin.com" !in link.host) continue

            val url = link.append("/raw")
            newPastes.add(createPaste(url.readContent()))
        }

        for (file in files) newPastes.add(createPaste(file))

        if (newPastes.isEmpty()) return

        val embed = Embed()
                .color(Color.SUCCESS)
                .field("Paste".plural(newPastes.size) + " converted!", newPastes.joinToString("\n"))

        message.textChannel.queueMessage(embed.build())
    }

    /**
     * Reads and gets the content of all the files in the message
     */
    private fun Message.getFiles(): List<String> {
        val files = mutableListOf<String>()

        for (attachment in attachments) {
            val file = attachment.downloadToFile().get()
            if (file.extension !in extensions) continue
            files.add(file.readContent())
            file.delete()
        }

        return files
    }

}