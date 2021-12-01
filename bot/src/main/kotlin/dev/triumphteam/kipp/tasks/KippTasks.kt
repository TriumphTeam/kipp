package dev.triumphteam.kipp.tasks

import dev.triumphteam.kipp.Kipp
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild

fun Kipp.updatePresence(guild: Guild) {
    println("Updating presence")
    // Add more later
    when ((0..2).random()) {
        0 -> jda.presence.setPresence(Activity.watching("${guild.members.size} members"), false)
        1 -> jda.presence.setPresence(Activity.playing("with your feelings"), false)
        2 -> jda.presence.setPresence(Activity.watching("messages"), false)
    }
}