package com.lokal.mume.domain.repository

import androidx.paging.PagingData
import com.lokal.mume.data.model.AlbumResult
import com.lokal.mume.data.model.ArtistDetailsResponse
import com.lokal.mume.data.model.ArtistResponse
import com.lokal.mume.data.model.ArtistResult
import com.lokal.mume.data.model.BaseResponse
import com.lokal.mume.data.model.SongResponse
import com.lokal.mume.data.model.SongResult
import com.lokal.mume.domain.model.SongModel
import kotlinx.coroutines.flow.Flow

interface NetworkCallRepo {
    suspend fun getHome()

    suspend fun searchArtist(query: String, limit: Int): ResultWrapper<ArtistResponse>

    suspend fun searchSongs(query: String, limit: Int): ResultWrapper<SongResponse>

    suspend fun saveHistory(song: SongModel)
    suspend fun getQueue(): List<SongModel>
    suspend fun generateNextSongs(): List<SongModel>

    suspend fun getHistory(): List<SongModel>

    fun getArtistPaging(query: String): Flow<PagingData<ArtistResult>>
    fun getSongsPaging(query: String, sort: String): Flow<PagingData<SongResult>>

    fun getAlbumsPaging(query: String): Flow<PagingData<AlbumResult>>
    fun getArtistSongsPaging(artistName: String): Flow<PagingData<SongResult>>
    suspend fun getArtistDetails(artistId: String): ResultWrapper<BaseResponse<ArtistDetailsResponse>>

    suspend fun addToQueue(song: SongModel, atTop: Boolean)

    suspend fun getNextInQueue(): SongModel?

    suspend fun removeFromQueue(songId: String)

    suspend fun getRandomSongs(): List<SongModel>

    suspend fun updateLastPosition(songId: String, position: Long)

    suspend fun clearQueue() // Added method
}


sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Failure(val errorMessage: String) : ResultWrapper<Nothing>()
}
