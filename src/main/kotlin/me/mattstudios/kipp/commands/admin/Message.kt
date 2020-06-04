package me.mattstudios.kipp.commands.admin

import com.google.gson.Gson
import me.mattstudios.kipp.json.JsonEmbed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.kipp.utils.Utils.append
import me.mattstudios.kipp.utils.Utils.isPaste
import me.mattstudios.kipp.utils.Utils.readContent
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.annotations.Requirement
import me.mattstudios.mfjda.base.CommandBase
import net.dv8tion.jda.api.entities.TextChannel
import java.net.URL

/**
 * @author Matt
 */
@Prefix("!")
@Command("message")
class Message : CommandBase() {

    private val gson = Gson()

    @Default
    @Requirement("#admin-up")
    fun message(channel: TextChannel?, link: URL?) {
        if (channel == null) return
        if (link == null || !link.isPaste()) {
            message.textChannel.queueMessage("That is not a valid link.")
            return
        }

        val paste = link.append("/raw")
        val pasteContent = paste.readContent()
        val jsonEmbed = gson.fromJson(pasteContent, JsonEmbed::class.java)

        channel.queueMessage(jsonEmbed.toEmbed(message.author))

    }

}