package com.lokal.mume.domain.model

import com.lokal.mume.data.model.ArtistImage

data class ArtistDetails(
    val id: String,
    val name: String,
    val role: String,
    val image: List<ArtistImage>,
    val type: String,
    val url: String
)
