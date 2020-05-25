package me.mattstudios.kipp.utils

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed

/**
 * @author Matt
 */
class Embed {

    private val embed = EmbedBuilder()

    init {
        embed.setColor(Utils.hexToRgb("#ef83a0"))
    }

    fun title(title: String): Embed {
        embed.setTitle(title)
        return this
    }

    fun footer(footer: String): Embed {
        embed.setFooter(footer)
        return this
    }

    fun footer(footer: String, icon: String): Embed {
        embed.setFooter(footer, icon)
        return this
    }

    fun color(color: String): Embed {
        embed.setColor(Utils.hexToRgb(color))
        return this
    }

    fun field(title: String, body: String): Embed {
        embed.addField(title, body, false)
        return this
    }

    fun field(title: String, body: String, inline: Boolean): Embed {
        embed.addField(title, body, inline)
        return this
    }

    fun thumbnail(url: String): Embed {
        embed.setThumbnail(url)
        return this
    }

    fun empty() {
        embed.addBlankField(false)
    }

    fun build(): MessageEmbed {
        return embed.build()
    }
}