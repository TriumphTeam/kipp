package dev.triumphteam.kipp.listener

import dev.triumphteam.bukkit.feature.feature
import dev.triumphteam.jda.JdaApplication
import dev.triumphteam.kipp.config.Config
import dev.triumphteam.kipp.config.KippColor
import dev.triumphteam.kipp.config.Settings
import dev.triumphteam.kipp.database.Messages
import dev.triumphteam.kipp.event.on
import dev.triumphteam.kipp.func.embed
import dev.triumphteam.kipp.func.queueMessage
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Logs messages into the database
 */
fun JdaApplication.messageListener() {
    val config = feature(Config)

    on<GuildMessageReceivedEvent> {
        if (author.isBot) return@on
        if (channel.id in config[Settings.MESSAGE_LOG_BLACKLISTED_CHANNELS]) return@on
        if (channel.parent?.id in config[Settings.MESSAGE_LOG_BLACKLISTED_CATEGORIES]) return@on

        transaction {
            Messages.insertIgnore {
                it[id] = messageIdLong
                it[content] = message.contentRaw
                it[sender] = author.idLong
            }
        }
    }

    on<GuildMessageUpdateEvent> {
        if (author.isBot) return@on
        if (channel.id in config[Settings.MESSAGE_LOG_BLACKLISTED_CHANNELS]) return@on
        if (channel.parent?.id in config[Settings.MESSAGE_LOG_BLACKLISTED_CATEGORIES]) return@on
        if (message.isSuppressedEmbeds) return@on

        val oldMessage = transaction {
            Messages.select { Messages.id eq message.idLong }.firstOrNull()
        } ?: return@on

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

    on<GuildMessageDeleteEvent> {
        if (channel.id in config[Settings.MESSAGE_LOG_BLACKLISTED_CHANNELS]) return@on
        if (channel.parent?.id in config[Settings.MESSAGE_LOG_BLACKLISTED_CATEGORIES]) return@on

        val message = transaction {
            Messages.select { Messages.id eq messageIdLong }.firstOrNull()
        } ?: return@on

        val author = jda.getUserById(message[Messages.sender]) ?: return@on
        if (author.isBot) return@on

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
}