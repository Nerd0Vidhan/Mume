package com.lokal.mume.data.remote

import com.lokal.mume.data.model.ArtistResponse
import com.lokal.mume.domain.repository.ResultWrapper
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkHelper {
    @GET("home")
    suspend fun getHome()

    @GET("search/songs")
    suspend fun searchSongs(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int = 20
    )/*: SongSearchResponse*/


    @GET("artists")
    suspend fun artists(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 10
    )

    @GET("search/artists")
    suspend fun searchArtist(
        @Query("query") query: String,
        @Query("limit") limit: Int = 20
    ): ArtistResponse
}
