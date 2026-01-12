package com.lokal.mume.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lokal.mume.data.mapper.toSongModel
import com.lokal.mume.data.model.SongResult
import com.lokal.mume.domain.model.SongModel
import com.lokal.mume.domain.repository.NetworkCallRepo
import com.lokal.mume.playback.MusicController
import com.lokal.mume.playback.PlaybackState
import com.lokal.mume.playback.PlaybackStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val controller: MusicController,
    private val repo: NetworkCallRepo
) : ViewModel() {

    val playbackState = PlaybackStateHolder.state

    private val _nextSongPreview = MutableStateFlow<SongModel?>(null)
    val nextSongPreview = _nextSongPreview.asStateFlow()

    init {
        loadLastState()
        preloadNext()
    }

    private fun loadLastState() {
        viewModelScope.launch {
            val history = repo.getHistory()
            if (history.isNotEmpty()) {
                val lastSong = history.first()
                PlaybackStateHolder.state.value = PlaybackState(
                    currentSong = lastSong,
                    isPlaying = false
                )
                controller.prepare(lastSong)
            }
        }
    }

    private fun preloadNext() {
        viewModelScope.launch {
            val nextInQueue = repo.getNextInQueue()
            if (nextInQueue != null) {
                controller.bufferNext(nextInQueue)
            } else {
                val random = repo.getRandomSongs().firstOrNull()
                random?.let { controller.bufferNext(it) }
            }
        }
    }

    fun dismissPlayer() {
        viewModelScope.launch {
            controller.pause()
            repo.clearQueue() // Clears only queue, keeping history intact
            PlaybackStateHolder.state.value = PlaybackState(
                currentSong = null,
                isPlaying = false
            )
        }
    }

    fun playNextInQueue(song: SongModel) {
        viewModelScope.launch {
            repo.addToQueue(song, atTop = true)
            preloadNext()
        }
    }

    fun addToQueueLast(song: SongModel) {
        viewModelScope.launch {
            repo.addToQueue(song, atTop = false)
            if (_nextSongPreview.value == null) preloadNext()
        }
    }

    fun playNext() {
        viewModelScope.launch {
            val queue = repo.getNextInQueue()
            if (queue != null) {
                controller.play(queue)
                repo.removeFromQueue(queue.id)
                repo.saveHistory(queue)
            } else {
                val randomSongs = repo.getRandomSongs()
                if (randomSongs.isNotEmpty()) {
                    val nextRandom = randomSongs.first()
                    controller.play(nextRandom)
                    repo.removeFromQueue(nextRandom.id)
                    randomSongs.drop(1).forEach {
                        repo.addToQueue(it, atTop = false)
                    }
                }
            }
            preloadNext()
        }
    }

    fun play(song: SongModel) {
        viewModelScope.launch {
            val currentState = PlaybackStateHolder.state.value
            if (currentState.currentSong?.id == song.id) {
                if (!currentState.isPlaying) {
                    controller.resume()
                }
            } else {
                controller.play(song)
                repo.saveHistory(song)
                preloadNext()
            }
        }
    }

    fun playRandom(songs: List<SongResult?>) {
        viewModelScope.launch {
            val validSongs = songs.filterNotNull()
            if (validSongs.isNotEmpty()) {
                val randomIndex = validSongs.indices.random()
                val randomSong = validSongs[randomIndex]
                play(randomSong.toSongModel())
                validSongs.toMutableList().apply {
                    removeAt(randomIndex)
                    forEach { repo.addToQueue(it.toSongModel(), atTop = false) }
                }
            }
        }
    }

    fun playAll(songs: List<SongResult?>) {
        viewModelScope.launch {
            val validSongs = songs.filterNotNull()
            if (validSongs.isNotEmpty()) {
                val firstSong = validSongs[0].toSongModel()
                play(firstSong)
                if (validSongs.size > 1) {
                    validSongs.drop(1).forEach { song ->
                        repo.addToQueue(song.toSongModel(), atTop = false)
                    }
                    controller.bufferNext(validSongs[1].toSongModel())
                }
            }
        }
    }

    fun addAllToQueue(songs: List<SongResult?>) {
        viewModelScope.launch {
            val validSongs = songs.filterNotNull()
            validSongs.forEach { song ->
                repo.addToQueue(song.toSongModel(), atTop = false)
            }
            if (playbackState.value.nextSong == null && validSongs.isNotEmpty()) {
                controller.bufferNext(validSongs[0].toSongModel())
            }
        }
    }

    fun pause() {
        controller.pause()
    }

    fun playPrevious() {
        viewModelScope.launch {
            val history = repo.getHistory()
            val previous = history.getOrNull(1) // Index 1 is the previous song
            previous?.let { play(it) }
        }
    }

    fun seekTo(positionMs: Float) {
        viewModelScope.launch {
            controller.seekTo(positionMs.toLong())
        }
    }

    fun seekForward() {
        viewModelScope.launch {
            controller.seekForward()
        }
    }

    fun seekBackward() {
        viewModelScope.launch {
            controller.seekBackward()
        }
    }
}
