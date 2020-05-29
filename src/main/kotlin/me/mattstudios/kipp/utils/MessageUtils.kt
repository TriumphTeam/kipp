package me.mattstudios.kipp.utils

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel

/**
 * @author Matt
 */
object MessageUtils {

    /**
     * Easier message queuing
     */
    fun TextChannel.queueMessage(message: Message) = sendMessage(message).queue()

    /**
     * Easier message queuing
     */
    fun TextChannel.queueMessage(message: CharSequence) = sendMessage(message).queue()

    /**
     * Easier message queuing
     */
    fun TextChannel.queueMessage(message: MessageEmbed) = sendMessage(message).queue()

    /**
     * Easier message queuing
     */
    fun MessageChannel.queueMessage(message: Message) = sendMessage(message).queue()

    /**
     * Easier message queuing
     */
    fun MessageChannel.queueMessage(message: CharSequence) = sendMessage(message).queue()

    /**
     * Easier message queuing
     */
    fun MessageChannel.queueMessage(message: MessageEmbed) = sendMessage(message).queue()

}
