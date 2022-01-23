package dev.triumphteam.kipp.commands.slash

import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.Command
import dev.triumphteam.cmd.core.annotation.Default
import dev.triumphteam.cmd.core.annotation.Description
import dev.triumphteam.cmd.slash.sender.SlashSender
import dev.triumphteam.kipp.button.Defer

@Command("docs")
@Description("Lists all available docs")
class DocsCommand : BaseCommand() {

    @Defer
    @Default
    fun SlashSender.docs() {
        reply("None").queue()
    }

}