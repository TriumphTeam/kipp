package me.mattstudios.kipp.json

import me.mattstudios.kipp.utils.Utils.fixLine
import net.dv8tion.jda.api.entities.MessageEmbed

/**
 * @author Matt
 */
data class JsonField(
        private val title: String,
        private val body: String,
        private val inline: Boolean = false
) {

    /**
     * Turns the json field into a message embed field
     */
    fun toField() = MessageEmbed.Field(title.fixLine(), body.fixLine(), inline)

}