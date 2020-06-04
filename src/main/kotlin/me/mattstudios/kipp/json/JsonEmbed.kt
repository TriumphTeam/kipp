package me.mattstudios.kipp.json

import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.Utils.fixLine
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User

/**
 * @author Matt
 */
data class JsonEmbed(
        val command: String?,
        private val title: String? = null,
        private val description: String? = null,
        private val fields: List<JsonField>? = null,
        private val image: String? = null,
        private val thumbnail: String? = null
) {

    /**
     * Parses the json embed into a Embed message
     */
    fun toEmbed(user: User? = null): MessageEmbed {
        val embed = Embed(user)

        if (title != null) embed.title(title.fixLine())
        if (description != null) embed.description(description.fixLine())
        if (image != null) embed.image(image)
        if (thumbnail != null) embed.thumbnail(thumbnail)

        fields?.forEach { embed.field(it.toField()) }

        return embed.build()
    }

}