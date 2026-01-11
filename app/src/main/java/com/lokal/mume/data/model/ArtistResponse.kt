package com.lokal.mume.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.lokal.mume.domain.model.ArtistDetails
import kotlinx.parcelize.Parcelize

data class ArtistResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: ArtistData
)

data class ArtistData(
    @SerializedName("total") val total: Int,
    @SerializedName("start") val start: Int,
    @SerializedName("results") val results: List<ArtistResult>
)

@Parcelize
data class ArtistResult(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("role") val role: String,
    @SerializedName("image") val image: List<ArtistImage>,
    @SerializedName("type") val type: String,
    @SerializedName("url") val url: String
): Parcelable {
    companion object{
        fun toDomain(artist: ArtistResult) = ArtistDetails(
            id = artist.id,
            name = artist.name,
            role = artist.role,
            image = artist.image,
            type = artist.type,
            url = artist.url)
    }
}

@Parcelize
data class ArtistImage(
    @SerializedName("quality") val quality: String,
    @SerializedName("url") val url: String
): Parcelable


data class ArtistDetailsResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("albumCount") val albumCount: Int?,
    @SerializedName("songCount") val songCount: Int?,
    // Saavn API often provides total play time or you can calculate it from top songs
    @SerializedName("topSongs") val topSongs: List<SongResult>
)
