package com.lokal.mume.domain.repository

import androidx.paging.PagingData
import com.lokal.mume.data.model.AlbumResult
import com.lokal.mume.data.model.ArtistResponse
import com.lokal.mume.data.model.ArtistResult
import com.lokal.mume.data.model.SongResponse
import com.lokal.mume.data.model.SongResult
import com.lokal.mume.domain.model.SongModel
import kotlinx.coroutines.flow.Flow

interface NetworkCallRepo {
    suspend fun getHome()//: /*HomeData*/

    suspend fun searchArtist(query: String, limit: Int): ResultWrapper<ArtistResponse>

    suspend fun searchSongs(query: String, limit: Int): ResultWrapper<SongResponse>

    suspend fun saveHistory(song: SongModel)
    suspend fun getQueue(): List<SongModel>
    suspend fun generateNextSongs(): List<SongModel>

    suspend fun getHistory(): List<SongModel>

    fun getArtistPaging(query: String): Flow<PagingData<ArtistResult>>
    fun getSongsPaging(query: String, sort: String): Flow<PagingData<SongResult>>

    fun getAlbumsPaging(query: String): Flow<PagingData<AlbumResult>>
}


sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Failure(val errorMessage: String) : ResultWrapper<Nothing>()
}
