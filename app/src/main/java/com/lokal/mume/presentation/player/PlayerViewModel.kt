package com.lokal.mume.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lokal.mume.data.mapper.toSongModel
import com.lokal.mume.domain.model.SongModel
import com.lokal.mume.domain.repository.NetworkCallRepo
import com.lokal.mume.playback.MusicController
import com.lokal.mume.playback.PlaybackState
import com.lokal.mume.playback.PlaybackStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val controller: MusicController,
    private val repo: NetworkCallRepo
) : ViewModel() {

    val playbackState = PlaybackStateHolder.state

    init {
        loadLastState()
    }

    private fun loadLastState() {
        viewModelScope.launch {
            val history = repo.getHistory()
            if (history.isNotEmpty()) {
                val lastSong = history.first()
                // Update state without auto-playing
                PlaybackStateHolder.state.value = PlaybackState(
                    currentSong = lastSong,
                    isPlaying = false
                )
            }
        }
    }

    fun play(song: SongModel) {
        viewModelScope.launch {
            controller.play(song)
            repo.saveHistory(song)
        }
    }

    fun pause() {
        controller.pause()
    }

    fun playNext() {
        viewModelScope.launch {
            val queue = repo.getQueue()
            val next = queue.firstOrNull()
                ?: repo.generateNextSongs().firstOrNull()

            next?.let { play(it) }
        }
    }

    fun playPrevious(){
        viewModelScope.launch {
            val queue = repo.getHistory()
            val previous = queue.firstOrNull()
            ?: repo.generateNextSongs().firstOrNull()

            previous?.let { play(it) }

        }
    }

    fun seekForward(){
        viewModelScope.launch {
            controller.seekForward()
        }
    }

    fun seekBackward(){
        viewModelScope.launch {
            controller.seekBackward()
        }
    }
}
