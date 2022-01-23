package dev.triumphteam.kipp.tasks

import dev.triumphteam.kipp.Kipp
import dev.triumphteam.kipp.database.Messages
import dev.triumphteam.kipp.func.kippInfo
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.TimeUnit

fun Kipp.deleteOldMessages() {
    transaction {
        val deleted =
            Messages.deleteWhere { Messages.timestamp less (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)) }

        kippInfo { "Deleted $deleted messages from database." }
    }
}