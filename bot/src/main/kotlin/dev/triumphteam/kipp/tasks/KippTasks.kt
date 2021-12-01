package dev.triumphteam.kipp.tasks

import dev.triumphteam.kipp.Kipp
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Activity

fun Kipp.updatePresence() {
    // Add more later
    when ((0..2).random()) {
        0 -> jda.presence.setPresence(Activity.watching("${jda.memberCount} members"), false)
        1 -> jda.presence.setPresence(Activity.playing("with your feelings"), false)
        2 -> jda.presence.setPresence(Activity.watching("messages"), false)
    }
}

private val JDA.memberCount: Int
    get() = guilds.sumOf { it.memberCount }