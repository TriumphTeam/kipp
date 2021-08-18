package dev.triumphteam.kipp.scheduler

import dev.triumphteam.jda.JdaApplication
import dev.triumphteam.kipp.database.Messages
import dev.triumphteam.kipp.func.kippInfo
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.TimeUnit

fun JdaApplication.checkOldMessages() {
    transaction {
        val deleted =
            Messages.deleteWhere { Messages.timestamp less (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)) }
        kippInfo { "Deleted $deleted messages from database." }
    }
}

fun JdaApplication.updatePresence(guild: Guild) {
    jda.presence.setPresence(Activity.watching("${guild.members.size} members"), false)
    // Add more later
    /*when ((0..2).random()) {
        0 -> jda.presence.setPresence(Activity.watching("${guild.members.size} members"), false)
        1 -> jda.presence.setPresence(Activity.playing("with your feelings"), false)
        2 -> jda.presence.setPresence(Activity.watching("messages"), false)
    }*/
}