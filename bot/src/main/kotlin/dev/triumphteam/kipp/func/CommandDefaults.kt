package dev.triumphteam.kipp.func

import dev.triumphteam.cmd.core.message.MessageKey
import dev.triumphteam.cmd.core.requirement.RequirementKey
import dev.triumphteam.core.jda.commands.PrefixedCommands
import net.dv8tion.jda.api.Permission

fun PrefixedCommands.registerDefaults() {
    message(MessageKey.UNKNOWN_COMMAND) { _, _ -> }

    requirement(RequirementKey.of("admin")) { it.member?.hasPermission(Permission.ADMINISTRATOR) == true }
}