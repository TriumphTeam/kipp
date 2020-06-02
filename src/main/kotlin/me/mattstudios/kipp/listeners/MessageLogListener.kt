package me.mattstudios.kipp.listeners

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.data.Database
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.kipp.utils.Color
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

/**
 * @author Matt
 */
class MessageLogListener(private val config: Config, private val cache: Cache, private val database: Database) : ListenerAdapter() {

    /**
     * Adds message to the database
     */
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (event.channel.id in config[Setting.BLACK_LISTED_CHANNELS]) return
        GlobalScope.launch { database.insertMessage(event.message) }
    }

    /**
     * Handles message deletions
     */
    override fun onGuildMessageDelete(event: GuildMessageDeleteEvent) {
        val channel = cache.messageChannel ?: return
        val message = database.getMessage(event.messageIdLong) ?: return
        val authorId = database.getMessageAuthor(event.messageIdLong) ?: return

        val user = event.jda.getUserById(authorId) ?: return
        if (user.isBot) return

        val embed = Embed()
                .color(Color.FAIL)
                .author(user.asTag, user.avatarUrl ?: user.defaultAvatarUrl)
                .description("Message sent by ${user.asMention} has been deleted.")
                .field("Message:", message)
                .field("Channel", event.channel.asMention)
                .footer("ID: ${user.id}")

        channel.queueMessage(embed.build())
    }

    /**
     * Handles message editing
     */
    override fun onGuildMessageUpdate(event: GuildMessageUpdateEvent) {
        val channel = cache.messageChannel ?: return
        val user = event.author
        val message = event.message

        val oldMessage = database.getMessage(event.messageIdLong)

        val embed = Embed().color(Color.EDIT)
                .author(user.asTag, user.avatarUrl ?: user.defaultAvatarUrl)
                .description("Message edited by ${user.asMention}")

        if (oldMessage != null) embed.field("Before:", oldMessage)

        embed.field("after:", message.contentRaw)
                .field("Channel", "[**#${message.textChannel.name}**](${message.jumpUrl})")
                .footer("ID: ${user.id}")

        channel.queueMessage(embed.build())

        GlobalScope.launch { database.insertMessage(event.message) }
    }

}