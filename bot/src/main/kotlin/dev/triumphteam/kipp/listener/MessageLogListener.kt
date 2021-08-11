package dev.triumphteam.kipp.listener

import dev.triumphteam.bukkit.feature.feature
import dev.triumphteam.jda.JdaApplication
import dev.triumphteam.kipp.config.Config
import dev.triumphteam.kipp.database.Messages
import dev.triumphteam.kipp.event.on
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Logs messages into the database
 */
fun JdaApplication.logMessages() {
    val config = feature(Config)

    on<GuildMessageReceivedEvent> {
        if (author.isBot) return@on

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

        transaction {
            val old = Messages.select { Messages.id eq message.idLong }.firstOrNull() ?: return@transaction
            channel.sendMessage("Edited from: `${old[Messages.content]}` to `${message.contentRaw}`.").queue()
        }

    }
}