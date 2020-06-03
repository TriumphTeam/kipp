package me.mattstudios.kipp.data

import me.mattstudios.kipp.utils.Embed
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User

/**
 * @author Matt
 */
data class JsonEmbed(
        val command: String,
        private val title: String? = null,
        private val description: String? = null,
        private val fields: MutableMap<String, JsonField>? = null,
        private val image: String? = null,
        private val thumbnail: String? = null
) {

    /**
     * Parses the json embed into a Embed message
     */
    fun toEmbed(user: User? = null): MessageEmbed {
        val embed = Embed(user)

        if (title != null) embed.title(title)
        if (description != null) embed.description(description)
        if (image != null) embed.image(image)
        if (thumbnail != null) embed.thumbnail(thumbnail)

        fields?.forEach { _, jsonField -> embed.field(jsonField.toField()) }

        return embed.build()
    }

}