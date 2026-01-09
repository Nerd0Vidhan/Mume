package com.lokal.mume.data.model

import com.google.gson.annotations.SerializedName
import com.lokal.mume.domain.model.ArtistDetails

data class ArtistResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: ArtistData
)

data class ArtistData(
    @SerializedName("total") val total: Int,
    @SerializedName("start") val start: Int,
    @SerializedName("results") val results: List<ArtistResult>
)

data class ArtistResult(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("role") val role: String,
    @SerializedName("image") val image: List<ArtistImage>,
    @SerializedName("type") val type: String,
    @SerializedName("url") val url: String
){
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

data class ArtistImage(
    @SerializedName("quality") val quality: String,
    @SerializedName("url") val url: String
)
