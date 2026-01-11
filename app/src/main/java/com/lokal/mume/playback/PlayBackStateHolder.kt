package com.lokal.mume.playback

import com.lokal.mume.domain.model.SongModel
import kotlinx.coroutines.flow.MutableStateFlow

object PlaybackStateHolder {
    val state = MutableStateFlow(PlaybackState())

    fun updateCurrentSong(song: SongModel) {
        state.value = state.value.copy(currentSong = song)
    }

    fun updateNextSong(song: SongModel?) {
        state.value = state.value.copy(nextSong = song)
    }
}
