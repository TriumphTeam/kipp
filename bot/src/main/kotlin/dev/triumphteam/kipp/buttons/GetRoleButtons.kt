package dev.triumphteam.kipp.buttons

import dev.triumphteam.core.feature.feature
import dev.triumphteam.kipp.Kipp
import dev.triumphteam.kipp.button.BaseButton
import dev.triumphteam.kipp.button.Button
import dev.triumphteam.kipp.button.Defer
import dev.triumphteam.kipp.config.Config
import dev.triumphteam.kipp.config.Settings
import dev.triumphteam.kipp.func.embed
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent

@Button("get-role")
@Defer(ephemeral = true)
class GetRoleButtons(kipp: Kipp) : BaseButton() {

    private val config = kipp.feature(Config)

    @Button("matt")
    fun ButtonClickEvent.getMattRole() {
        val guild = guild ?: return
        val member = member ?: return

        val mattRole = guild.getRoleById(config[Settings.ROLES].matt) ?: return

        if (mattRole in member.roles) guild.removeRoleFromMember(member, mattRole).queue()
        else guild.addRoleToMember(member, mattRole).queue()

       val embed = embed {
            description("Hello")
            color("#00ff00")

            fields {
                field("Field 1", "Value 1", true)
                field("Field 2", "Value 2", true)
                empty()
                field("Field 3", "Value 3")
            }

            author(user)
        }

        hook.editOriginalEmbeds(embed).queue()
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