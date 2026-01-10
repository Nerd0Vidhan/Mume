package com.lokal.mume.playback

import com.lokal.mume.domain.model.SongModel

data class PlaybackState(
    val currentSong: SongModel? = null,
    val isPlaying: Boolean = false,
    val position: Long = 0L,
    val duration: Long = 0L
)
