package me.mattstudios.kipp.listeners

import com.google.cloud.dialogflow.v2.QueryInput
import com.google.cloud.dialogflow.v2.SessionName
import com.google.cloud.dialogflow.v2.SessionsClient
import com.google.cloud.dialogflow.v2.TextInput
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.mattstudios.kipp.Kipp
import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.data.Database
import me.mattstudios.kipp.player.MusicPlayer
import me.mattstudios.kipp.scheduler.Scheduler
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.kipp.utils.Utils
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.managers.AudioManager
import java.time.format.DateTimeParseException


/**
 * @author Matt
 */
class KippListener(
        private val cache: Cache,
        config: Config,
        private val database: Database,
        private val scheduler: Scheduler,
        private val playerManager: AudioPlayerManager
) : ListenerAdapter() {

    private val client: SessionsClient = SessionsClient.create()
    private val session: SessionName = SessionName.of(config[Setting.DIALOGFLOW_PROJECT], (100000000..999999999).random().toString())

    private var musicPlayer: MusicPlayer? = null

    //private val youtube = Youtube

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val message = event.message
        val selfUser = event.jda.selfUser
        val channel = event.channel
        val user = event.author
        val member = event.member ?: return
        if (selfUser !in message.mentionedUsers) return

        val messageContent = message.contentDisplay.replace("@${selfUser.name} ", "")

        /*if (messageContent.startsWith("remind me", true)) {
            val adminRole = cache.adminRole ?: return

            if (member.roles.none() { role -> role.position >= adminRole.position }) {
                channel.queueMessage(Embed(user).field("No!", "You're not a lowed to set reminders!").build())
                return
            }
        }*/



        if (messageContent.length > 255) {
            channel.queueMessage(Embed(user).description("Could not process your request, your message is too big!").build())
            return
        }

        GlobalScope.launch {
            val response = handleDialogFlow(messageContent).split("|")
            if (response.isEmpty()) return@launch

            when (response[0]) {
                "reminder" -> handleReminder(response.drop(1), user, channel)
                "play-url" -> handlePlay(response.drop(1), user, channel)
                "play-search" -> handlePlay(response.drop(1), user, channel, false)
                "lower-volume" -> handleVolume(channel, VolumeType.LOWER)
                "higher-volume" -> handleVolume(channel, VolumeType.HIGHER)
                "connect" -> handleConnect(response.drop(1), user, channel)
                "disconnect" -> handleDisconnect(response.drop(1), user, channel)
                //else -> channel.queueMessage(response.joinToString(" "))
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
     * Handles the reminder stuff
     */
    private fun handlePlay(response: List<String>, user: User, channel: TextChannel, url: Boolean = true) {
        if (response.isEmpty()) return

        if (response[0] == "error") {
            channel.queueMessage(Embed(user).field("Music", "Could not play song, perhaps you didn't specify a name or URL?").build())
            return
        }

        val musicPlayer = getMusicPlayer(channel.guild) ?: return
        playSong(musicPlayer, response[0].checkUrl(url), channel)
    }

    /**
     * Handles the reminder stuff
     */
    private fun handleConnect(response: List<String>, user: User, channel: TextChannel) {
        if (response.isEmpty()) return

        val audioManager = channel.guild.audioManager

        // No channel has been specified
        if (response[0] == "error") {
            if (!audioManager.connectToFirstChannel()) {
                if (audioManager.isConnected || audioManager.isAttemptingToConnect) {
                    channel.queueMessage(
                            Embed(user)
                                    .field("Music", "I'm already connected to a channel!")
                                    .build()
                    )
                } else {
                    channel.queueMessage(
                            Embed(user)
                                    .field("Music", "Couldn't find any channel to connect to or there is no one in the channel!")
                                    .build()
                    )
                }

                return
            }

            channel.queueMessage(
                    Embed(user)
                            .field("Music", "Sure thing!")
                            .build()
            )

            return
        }


        if (audioManager.connectToChannel(response[0])) {
            channel.queueMessage(
                    Embed(user)
                            .field("Music", "Sure thing!")
                            .build()
            )

            return
        }

        channel.queueMessage(
                Embed(user)
                        .field("Music", "Couldn't find the channel ${response[0]} :c")
                        .build()
        )
    }

    private fun playSong(musicPlayer: MusicPlayer, track: String, channel: TextChannel) {
        playerManager.loadItemOrdered(musicPlayer, track, object : AudioLoadResultHandler {

            override fun trackLoaded(track: AudioTrack) {
                channel.queueMessage(
                        Embed()
                                .field("Music", "Adding to queue ${track.info.title}")
                                .build()
                )

                musicPlayer.queueTrack(track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                var firstTrack = playlist.selectedTrack

                if (firstTrack == null) firstTrack = playlist.tracks.first()


                channel.queueMessage(
                        Embed()
                                .field("Music", "Adding to queue " + firstTrack.info.title + " (first track of playlist " + playlist.name + ")")
                                .build()
                )

                musicPlayer.queueTrack(firstTrack)
            }

            override fun noMatches() {
                channel.sendMessage("Nothing found").queue()
            }

            override fun loadFailed(exception: FriendlyException) {
                channel.sendMessage("Could not play: " + exception.message).queue()
            }
        })

    }

    /**
     * Handles the reminder stuff
     */
    private fun handleDisconnect(response: List<String>, user: User, channel: TextChannel) {
        if (response.isEmpty()) return

        val audioManager = channel.guild.audioManager

        if (!audioManager.isConnected) {
            channel.queueMessage(
                    Embed(user)
                            .field("Music", "I am not connected to any channel!")
                            .build()
            )
            return
        }

        audioManager.disconnect()

        channel.queueMessage(
                Embed(user)
                        .field("Music", "Disconnected!")
                        .build()
        )
    }

    private fun handleVolume(channel: TextChannel, volumeType: VolumeType, value: Int = 5) {
        val musicPlayer = getMusicPlayer(channel.guild) ?: return

        when (volumeType) {
            VolumeType.HIGHER -> musicPlayer.raiseVolume(value)
            VolumeType.LOWER -> musicPlayer.lowerVolume(value)
        }
    }

    @Synchronized
    private fun getMusicPlayer(guild: Guild): MusicPlayer? {
        if (musicPlayer != null) return musicPlayer

        val newPlayer = MusicPlayer(playerManager)
        musicPlayer = newPlayer

        guild.audioManager.sendingHandler = newPlayer.getSendHandler()
        return newPlayer
    }

    /**
     * Connects to the first channel
     */
    private fun AudioManager.connectToFirstChannel(): Boolean {
        if (isConnected || isAttemptingToConnect) return false

        for (voiceChannel in guild.voiceChannels) {
            if (voiceChannel.members.size == 0) continue
            openAudioConnection(voiceChannel)
            return true
        }

        return false
    }

    /**
     * Connects to the first channel
     */
    private fun AudioManager.connectToChannel(channel: String): Boolean {
        if (isConnected || isAttemptingToConnect) return false

        val voiceChannel = guild.getVoiceChannelsByName(channel, true).firstOrNull() ?: return false
        openAudioConnection(voiceChannel)

        return true
    }

    /**
     * Disconnect from the channel
     */
    private fun AudioManager.disconnect() = closeAudioConnection()

    /**
     * Easier way to use the TextInput class
     */
    private fun String.toTextInput() = TextInput.newBuilder().setText(this).setLanguageCode("en-US")

    /**
     * Easier way to use the query input class
     */
    private fun TextInput.Builder.queryInput() = QueryInput.newBuilder().setText(this).build()

    private fun String.checkUrl(url: Boolean) = if (url) this else "ytsearch:$this"

    private enum class VolumeType {
        LOWER,
        HIGHER,
        SET
    }

}