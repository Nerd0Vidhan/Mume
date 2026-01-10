package com.lokal.mume.playback

import kotlinx.coroutines.flow.MutableStateFlow

object PlaybackStateHolder {
    val state = MutableStateFlow(PlaybackState())
}
