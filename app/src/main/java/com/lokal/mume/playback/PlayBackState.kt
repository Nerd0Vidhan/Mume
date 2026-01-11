package com.lokal.mume.playback

import com.lokal.mume.domain.model.SongModel

data class PlaybackState(
    val currentSong: SongModel? = null,
    val nextSong: SongModel? = null, // Added for lag-free UI preview
    val isPlaying: Boolean = false,
    val position: Long = 0L,
    val duration: Long = 0L
)
