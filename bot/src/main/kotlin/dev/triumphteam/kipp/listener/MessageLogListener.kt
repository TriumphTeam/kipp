package dev.triumphteam.kipp.listener

import dev.triumphteam.core.feature.feature
import dev.triumphteam.kipp.Kipp
import dev.triumphteam.kipp.config.Config
import dev.triumphteam.kipp.config.KippColor
import dev.triumphteam.kipp.config.Settings
import dev.triumphteam.kipp.database.Messages
import dev.triumphteam.kipp.func.embed
import dev.triumphteam.kipp.func.queueMessage
import net.dv8tion.jda.api.entities.Channel
import net.dv8tion.jda.api.entities.ICategorizableChannel
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Logs messages into the database
 */
class MessageLogListener(kipp: Kipp) : ListenerAdapter() {
    private val config = kipp.feature(Config)

    override fun onMessageReceived(event: MessageReceivedEvent): Unit = with(event) {
        if (author.isBot) return
        if (isBlackListed(channel)) return

        transaction {
            Messages.insertIgnore {
                it[id] = messageIdLong
                it[content] = message.contentRaw
                it[sender] = author.idLong
            }
        }
    }

    override fun onMessageUpdate(event: MessageUpdateEvent): Unit = with(event) {
        if (author.isBot) return
        if (isBlackListed(channel)) return
        if (message.isSuppressedEmbeds) return

        val oldMessage = transaction {
            Messages.select { Messages.id eq message.idLong }.firstOrNull()
        } ?: return

        val embed = embed {
            color(KippColor.EDIT)
            author(author)
            description("Message edited by ${author.asMention}")
            field("**Before**:", oldMessage[Messages.content])
            field("**after**:", message.contentRaw)
            field("**Channel**:", "[**#${message.textChannel.name}**](${message.jumpUrl})")
            footer("ID: ${author.id}")
        }

        jda.getTextChannelById(config[Settings.CHANNELS].messages)?.queueMessage(embed)
    }

    override fun onMessageDelete(event: MessageDeleteEvent): Unit = with(event) {
        if (isBlackListed(channel)) return

        val message = transaction {
            Messages.select { Messages.id eq messageIdLong }.firstOrNull()
        } ?: return

        val author = jda.getUserById(message[Messages.sender]) ?: return
        if (author.isBot) return

        val embed = embed {
            color(KippColor.FAIL)
            author(author)
            description("Message sent by ${author.asMention} has been deleted.")
            field("Message", message[Messages.content])
            field("Channel", channel.asMention)
            footer("ID: ${author.id}")
        }

        jda.getTextChannelById(config[Settings.CHANNELS].messages)?.queueMessage(embed)
    }

    /**
     * Checks if the channel is blacklisted.
     */
    private fun isBlackListed(channel: Channel): Boolean {
        if (channel.id in config[Settings.MESSAGE_LOG_BLACKLISTED_CHANNELS]) return true

        if (channel is ICategorizableChannel) {
            if (channel.parentCategoryId in config[Settings.MESSAGE_LOG_BLACKLISTED_CATEGORIES]) return true
        }

        return false
    }

}