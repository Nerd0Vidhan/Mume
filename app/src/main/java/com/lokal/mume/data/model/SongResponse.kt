package com.lokal.mume.data.model

import com.google.gson.annotations.SerializedName

data class SongResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: SongData
)


data class SongData(
    @SerializedName("total") val total: Int,
    @SerializedName("start") val start: Int,
    @SerializedName("results") val results: List<SongResult>
)
data class SongResult(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("year") val year: String?,
    @SerializedName("releaseDate") val releaseDate: String?,
    @SerializedName("duration") val duration: Int,
    @SerializedName("label") val label: String?,
    @SerializedName("explicitContent") val explicitContent: Boolean,
    @SerializedName("playCount") val playCount: Long,
    @SerializedName("language") val language: String,
    @SerializedName("hasLyrics") val hasLyrics: Boolean,
    @SerializedName("lyricsId") val lyricsId: String?,
    @SerializedName("url") val url: String,
    @SerializedName("copyright") val copyright: String?,
    @SerializedName("album") val album: SongAlbum,
    @SerializedName("artists") val artists: SongArtists,
    @SerializedName("image") val image: List<ArtistImage>,
    @SerializedName("downloadUrl") val downloadUrl: List<SongDownloadUrl>
)

data class SongAlbum(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

data class SongArtists(
    @SerializedName("primary") val primary: List<ArtistResult>,
    @SerializedName("featured") val featured: List<ArtistResult>,
    @SerializedName("all") val all: List<ArtistResult>
)


data class SongDownloadUrl(
    @SerializedName("quality") val quality: String,
    @SerializedName("url") val url: String
)


data class DownloadUrl(
    val quality: String,
    val url: String
)
