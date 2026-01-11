package com.lokal.mume.data.model

import com.google.gson.annotations.SerializedName

data class AlbumResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: AlbumData
)

data class AlbumData(
    @SerializedName("total") val total: Int,
    @SerializedName("start") val start: Int,
    @SerializedName("results") val results: List<AlbumResult>
)

data class AlbumResult(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("year") val year: String?,
    @SerializedName("type") val type: String,
    @SerializedName("playCount") val playCount: Long?,
    @SerializedName("language") val language: String?,
    @SerializedName("explicitContent") val explicitContent: Boolean,
    @SerializedName("url") val url: String,
    @SerializedName("songCount") val songCount: String?,
    @SerializedName("artists") val artists: AlbumArtists?,
    @SerializedName("image") val image: List<ArtistImage> // Reusing ArtistImage model
)

data class AlbumArtists(
    @SerializedName("primary") val primary: List<ArtistResult>,
    @SerializedName("featured") val featured: List<ArtistResult>,
    @SerializedName("all") val all: List<ArtistResult>
)