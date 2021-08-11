package dev.triumphteam.kipp.listener

import dev.triumphteam.kipp.database.Messages
import dev.triumphteam.kipp.event.Listeners
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Logs messages into the database
 */
fun Listeners.logMessage() {
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
}