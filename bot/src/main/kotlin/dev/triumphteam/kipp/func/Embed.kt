package dev.triumphteam.kipp.func

import dev.triumphteam.kipp.config.KippColor
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import java.time.Instant

class Embed(user: User? = null, timestamp: Boolean = false) {

    private val embed = EmbedBuilder()
    private var footer: String = ""
    private val userImage = user?.avatarUrl ?: user?.defaultAvatarUrl

    init {
        embed.setColor(KippColor.DEFAULT.code.toColor())

        if (user != null) {
            footer = "Requested by: ${user.asTag}"
            embed.setFooter(footer, userImage)
        }

        embed.setTimestamp(Instant.now())
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

    fun appendFooter(footer: String): Embed {
        this.footer += footer
        embed.setFooter(this.footer, userImage)
        return this
    }

    fun color(hex: String): Embed {
        embed.setColor(hex.toColor())
        return this
    }

    fun color(color: KippColor): Embed {
        embed.setColor(color.code.toColor())
        return this
    }

    fun field(title: String, body: String): Embed {
        embed.addField(title, body, false)
        return this
    }

    fun field(field: MessageEmbed.Field): Embed {
        embed.addField(field)
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

    fun thumbnail(user: User): Embed {
        embed.setThumbnail(user.avatarUrl ?: user.defaultAvatarUrl)
        return this
    }

    fun empty(): Embed {
        embed.addBlankField(false)
        return this
    }

    fun image(url: String): Embed {
        embed.setImage(url)
        return this
    }

    fun author(author: String): Embed {
        embed.setAuthor(author)
        return this
    }

    fun author(author: String, image: String): Embed {
        embed.setAuthor(author, null, image)
        return this
    }

    fun author(author: String, user: User): Embed {
        embed.setAuthor(author, null, user.avatarUrl ?: user.defaultAvatarUrl)
        return this
    }

    fun author(author: User): Embed {
        embed.setAuthor(author.asTag, null, author.avatarUrl ?: author.defaultAvatarUrl)
        return this
    }

    fun description(description: String): Embed {
        embed.setDescription(description)
        return this
    }

    fun build(): MessageEmbed {
        return embed.build()
    }

}

inline fun embed(user: User? = null, timestamp: Boolean = false, builder: Embed.() -> Unit): MessageEmbed {
    return Embed(user, timestamp).apply(builder).build()
}