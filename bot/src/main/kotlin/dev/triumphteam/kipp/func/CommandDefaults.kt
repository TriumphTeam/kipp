package dev.triumphteam.kipp.func

import dev.triumphteam.cmd.core.message.MessageKey
import dev.triumphteam.core.jda.commands.PrefixedCommands

fun PrefixedCommands.registerDefaults() {
    message(MessageKey.UNKNOWN_COMMAND) { _, _ -> }
}