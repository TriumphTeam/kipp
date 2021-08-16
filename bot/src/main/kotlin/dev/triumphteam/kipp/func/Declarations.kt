package dev.triumphteam.kipp.func

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import java.awt.Color
import java.net.URL

fun tokenFromFlag(args: Array<String>): String {
    val cli = DefaultParser().parse(
        Options().apply {
            addOption(Option.builder("t").hasArg().argName("token").required().build())
        },
        args
    )

    return cli.getOptionValue("t")
}

fun String.toColor(): Color {
    return Color(
        Integer.valueOf(substring(1, 3), 16),
        Integer.valueOf(substring(3, 5), 16),
        Integer.valueOf(substring(5, 7), 16)
    )
}

fun TextChannel.queueMessage(message: Message) = sendMessage(message).queue()
fun TextChannel.queueMessage(message: MessageEmbed) = sendMessageEmbeds(message).queue()
fun Message.queueReply(message: Message, mention: Boolean = false) = reply(message).mentionRepliedUser(mention).queue()
fun Message.queueReply(message: MessageEmbed, mention: Boolean = false) =
    replyEmbeds(message).mentionRepliedUser(mention).queue()

fun URL.append(string: String) = if (this.path == null || "/raw" in this.path) this else URL(this, string + this.path)

/**
 * Makes the word plural if the count is not one
 */
fun String.plural(count: Int) = if (count != 1) plus("s") else this