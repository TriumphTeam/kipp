package dev.triumphteam.kipp.buttons

import dev.triumphteam.core.feature.feature
import dev.triumphteam.kipp.Kipp
import dev.triumphteam.kipp.button.BaseButton
import dev.triumphteam.kipp.button.Button
import dev.triumphteam.kipp.button.Defer
import dev.triumphteam.kipp.config.Config
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent

@Button("get-role")
@Defer(ephemeral = true)
class GetRoleButtons(kipp: Kipp) : BaseButton() {

    private val config = kipp.feature(Config)

    @Button("matt")
    fun ButtonClickEvent.getMattRole() {
        hook.editOriginalEmbeds(EmbedBuilder().setDescription("ass").build()).queue()
    }

    @Button("lib")
    fun ButtonClickEvent.getLibRole() {
        deferReply().queue()
        hook.editOriginal("Hell yeah, you clicked - lib!").queue()
    }

    @Button("extra")
    fun ButtonClickEvent.getExtraRole() {
        deferReply().queue()
        hook.editOriginal("Hell yeah, you clicked - extra!").queue()
    }
}