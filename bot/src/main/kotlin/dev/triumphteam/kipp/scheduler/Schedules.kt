package dev.triumphteam.kipp.scheduler

import dev.triumphteam.kipp.database.Messages
import dev.triumphteam.kipp.func.log
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.TimeUnit

fun checkOldMessages() {
    transaction {
        log { "Deleting old messages." }
        Messages.deleteWhere { Messages.timestamp less (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)) }
        log { "Messages deleted." }
    }
}