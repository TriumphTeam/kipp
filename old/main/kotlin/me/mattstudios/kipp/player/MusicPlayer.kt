package me.mattstudios.kipp.player

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import java.util.concurrent.LinkedBlockingQueue


/**
 * @author Matt
 */
class MusicPlayer(playerManager: AudioPlayerManager) : AudioEventAdapter() {
    private val player = playerManager.createPlayer()
    private val queue = LinkedBlockingQueue<AudioTrack>()

    init {
        player.addListener(this)
        player.volume = 20
    }

    /**
     * Queue's the track
     */
    fun queueTrack(track: AudioTrack) {
        if (!player.startTrack(track, true)) queue.offer(track)
    }

    /**
     * Goes to the next track
     */
    fun nextTrack() {
        player.startTrack(queue.poll(), false)
    }

    /**
     * Runs when track ends
     */
    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) nextTrack()
    }

    fun getSendHandler(): AudioPlayerSendHandler {
        return AudioPlayerSendHandler(player)
    }

    fun raiseVolume(amount: Int) {
        player.volume = (player.volume + amount).coerceAtMost(100)
    }

    fun lowerVolume(amount: Int) {
        player.volume = (player.volume - amount).coerceAtLeast(0)
    }

}