package com.lokal.mume.playback

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaStyleNotificationHelper
import com.lokal.mume.MainActivity
import com.lokal.mume.domain.model.SongModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.media3.session.MediaSession
import com.lokal.mume.domain.repository.NetworkCallRepo
import javax.inject.Inject

@AndroidEntryPoint
class MusicService : Service() {

    private val binder = MusicBinder()
    lateinit var player: ExoPlayer

    @Inject
    lateinit var repo: NetworkCallRepo
    private var mediaSession: MediaSession? = null

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var progressJob: Job? = null

    inner class MusicBinder : Binder() {
        fun service(): MusicService = this@MusicService
    }

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        
        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(
                PendingIntent.getActivity(
                    this, 0, Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()

        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updateState()
                if (isPlaying) startProgressUpdate() else stopProgressUpdate()
            }
            
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    updateState()
                }
            }
        })

        startForeground(1, createNotification())
    }
    override fun onTaskRemoved(rootIntent: Intent?) {
        // Save current position to history when user kills the app
        val currentSong = PlaybackStateHolder.state.value.currentSong
        if (currentSong != null) {
            val position = player.currentPosition
            serviceScope.launch(Dispatchers.IO) {
                repo.updateLastPosition(currentSong.id, position)
            }
        }
        super.onTaskRemoved(rootIntent)
    }

    private fun updateState() {
        val current = PlaybackStateHolder.state.value
        PlaybackStateHolder.state.value = current.copy(
            isPlaying = player.isPlaying,
            duration = player.duration.coerceAtLeast(0L),
            position = player.currentPosition
        )
    }

    private fun startProgressUpdate() {
        progressJob?.cancel()
        progressJob = serviceScope.launch {
            while (true) {
                updateState()
                delay(1000)
            }
        }
    }

    private fun stopProgressUpdate() {
        progressJob?.cancel()
    }

    fun play(song: SongModel) {
        val mediaMetadata = MediaMetadata.Builder()
            .setTitle(song.title)
            .setArtist(song.artist)
            .setArtworkUri(android.net.Uri.parse(song.artwork))
            .build()

        val mediaItem = MediaItem.Builder()
            .setUri(song.streamUrl320)
            .setMediaId(song.id)
            .setMediaMetadata(mediaMetadata)
            .build()

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        PlaybackStateHolder.state.value = PlaybackState(
            currentSong = song,
            isPlaying = true,
            duration = 0L,
            position = 0L
        )
    }
    fun bufferNext(song: SongModel) {
        // Adds to the end of the current playlist for seamless transition
        player.addMediaItem(MediaItem.fromUri(song.streamUrl320))
    }

    fun prepare(song: SongModel) {
        player.setMediaItem(MediaItem.fromUri(song.streamUrl320))
        player.prepare() // Pre-loads data but doesn't play
    }

    fun pause() {
        player.pause()
    }

    fun resume() {
        player.play()
    }

    fun seekTo(position: Long) {
        player.seekTo(position)
        updateState()
    }

    fun seekForward() {
        player.seekTo(player.currentPosition + 10000)
        updateState()
    }

    fun seekBackward() {
        player.seekTo(player.currentPosition - 10000)
        updateState()
    }

    override fun onBind(intent: Intent?): IBinder = binder

    @OptIn(UnstableApi::class)
    private fun createNotification(): Notification {
        val builder = androidx.core.app.NotificationCompat.Builder(this, "playback")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle("Mume")
            .setContentText("Ready to play")
            .setStyle(MediaStyleNotificationHelper.MediaStyle(mediaSession!!))
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_LOW)
            .setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
        
        return builder.build()
    }

    override fun onDestroy() {
        mediaSession?.release()
        player.release()
        stopProgressUpdate()
        super.onDestroy()
    }
}
