package me.mattstudios.kipp.listeners

import com.google.cloud.dialogflow.v2.QueryInput
import com.google.cloud.dialogflow.v2.SessionName
import com.google.cloud.dialogflow.v2.SessionsClient
import com.google.cloud.dialogflow.v2.TextInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.mattstudios.kipp.Kipp
import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.data.Database
import me.mattstudios.kipp.scheduler.Scheduler
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.kipp.utils.Utils
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.time.format.DateTimeParseException


/**
 * @author Matt
 */
class KippListener(
        private val cache: Cache,
        private val config: Config,
        private val database: Database,
        private val scheduler: Scheduler
) : ListenerAdapter() {

    private val client = SessionsClient.create()
    private val session = SessionName.of(config[Setting.DIALOGFLOW_PROJECT], (100000000..999999999).random().toString())

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val message = event.message
        val selfUser = event.jda.selfUser
        val channel = event.channel
        val user = event.author
        val member = event.member ?: return
        if (selfUser !in message.mentionedUsers) return

        val messageContent = message.contentDisplay.replace("@${selfUser.name} ", "")

        if (messageContent.startsWith("remind me", true)) {
            val adminRole = cache.adminRole ?: return

            if (member.roles.none() { role -> role.position >= adminRole.position }) {
                channel.queueMessage(Embed(user).field("No!", "You're not a lowed to set reminders!").build())
                return
            }
        }

        if (messageContent.length > 255) {
            channel.queueMessage(Embed(user).description("Could not process your request, your message is too big!").build())
            return
        }

        GlobalScope.launch {
            val response = handleDialogFlow(messageContent).split("|")
            if (response.isEmpty()) return@launch

            when (response[0]) {
                "reminder" -> handleReminder(response.drop(1), user, channel)
            }
        }

    }

    /**
     * Handles the dialog flow request
     */
    private suspend fun handleDialogFlow(message: String): String {
        return withContext(Dispatchers.IO) {
            Kipp.logger.info("Dialog flow requested!")
            val response = client.detectIntent(session, message.toTextInput().queryInput())
            Kipp.logger.info("Waiting for response..")

            return@withContext response.queryResult.fulfillmentText
        }
    }

    /**
     * Handles the reminder stuff
     */
    private fun handleReminder(response: List<String>, user: User, channel: TextChannel) {
        if (response.isEmpty()) return

        if (response[0] == "error") {
            channel.queueMessage(Embed(user).field("Reminder", "Could not set a reminder, perhaps you didn't specify a date and time?").build())
            return
        }

        val (dateArg, task) = response
        val date = try {
            Utils.dateFormat.parse(dateArg)
        } catch (e: DateTimeParseException) {
            channel.queueMessage(Embed(user).field("Reminder", "Could not set a reminder, perhaps you didn't specify the time?").build())
            return
        }

        channel.queueMessage(Embed(user).description("Sure thing ${user.asMention}, I'll remind you to `$task`!").build())

        database.insertReminder(date, user.idLong, task)

        // TODO fix the reminders and add TIME ZONE

        val reminderChannel = cache.reminderChannel ?: return

        scheduler.scheduleTask(date) {
            reminderChannel.queueMessage(
                    Embed().description(
                            "Hey ${user.asMention} reminding you to:\n" +
                            "```\n" +
                            task +
                            "\n```")
                            .build()
            )
        }
    }

    /**
     * Easier way to use the TextInput class
     */
    private fun String.toTextInput() = TextInput.newBuilder().setText(this).setLanguageCode("en-US")

    /**
     * Easier way to use the query input class
     */
    private fun TextInput.Builder.queryInput() = QueryInput.newBuilder().setText(this).build()

}