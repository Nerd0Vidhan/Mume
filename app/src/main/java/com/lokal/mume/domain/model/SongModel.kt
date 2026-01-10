package com.lokal.mume.domain.model

data class SongModel(
    val id: String,
    val title: String,
    val artist: String,
    val genre: String,
    val streamUrl320: String,
    val artwork: String
)

