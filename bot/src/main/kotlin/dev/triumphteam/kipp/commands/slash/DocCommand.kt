package dev.triumphteam.kipp.commands.slash

import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.ArgName
import dev.triumphteam.cmd.core.annotation.Command
import dev.triumphteam.cmd.core.annotation.Description
import dev.triumphteam.cmd.core.annotation.SubCommand
import dev.triumphteam.cmd.slash.sender.SlashSender
import dev.triumphteam.kipp.button.Defer

@Command("doc")
class DocCommand : BaseCommand() {

    @Defer
    @SubCommand("info")
    @Description("Lists all methods/fields for the class")
    fun SlashSender.info(infoType: InfoType, @ArgName("class") klass: String) {
        reply("Checking methods for $klass").queue()
    }

    @Defer
    @SubCommand("method")
    @Description("Gets the documentation for a method")
    fun SlashSender.method(@ArgName("class") klass: String, method: String) {
        reply("Checking $method in class $klass").queue()
    }

    @Defer
    @SubCommand("field")
    @Description("Gets the documentation for a field")
    fun SlashSender.field(@ArgName("class") klass: String, field: String) {
        reply("Checking $field in class $klass").queue()
    }

}

enum class InfoType {
    FIELDS,
    METHODS
}