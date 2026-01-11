package com.lokal.mume.data.remote

import com.lokal.mume.data.model.AlbumResult
import com.lokal.mume.data.model.ArtistDetailsResponse
import com.lokal.mume.data.model.ArtistResponse
import com.lokal.mume.data.model.ArtistResult
import com.lokal.mume.data.model.BaseResponse
import com.lokal.mume.data.model.PaginatedData
import com.lokal.mume.data.model.SongResponse
import com.lokal.mume.data.model.SongResult
import com.lokal.mume.domain.repository.ResultWrapper
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkHelper {
    @GET("home")
    suspend fun getHome()


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

    @GET("search/songs")
    suspend fun randomSongsByGenre(
        @Query("query") query: String,
        @Query("limit") limit: Int = 5
    ): SongResponse

    @GET("search/songs")
    suspend fun searchSongs(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("sort") sort: String = "asc"
    ): BaseResponse<PaginatedData<SongResult>>

    @GET("search/artists")
    suspend fun searchArtist(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): BaseResponse<PaginatedData<ArtistResult>>

    @GET("search/albums")
    suspend fun searchAlbums(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): BaseResponse<PaginatedData<AlbumResult>>

    @GET("artists")
    suspend fun getArtistDetails(
        @Query("id") artistId: String
    ): BaseResponse<ArtistDetailsResponse>

}
