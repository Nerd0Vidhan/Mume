package com.lokal.mume.domain.repository

import com.lokal.mume.data.model.ArtistResponse
import com.lokal.mume.data.model.SongResponse
import com.lokal.mume.domain.model.SongModel

interface NetworkCallRepo {
    suspend fun getHome()//: /*HomeData*/

    suspend fun searchArtist(query: String, limit: Int): ResultWrapper<ArtistResponse>

    suspend fun searchSongs(query: String, limit: Int): ResultWrapper<SongResponse>

    suspend fun saveHistory(song: SongModel)
    suspend fun getQueue(): List<SongModel>
    suspend fun generateNextSongs(): List<SongModel>

    suspend fun getHistory(): List<SongModel>
}


sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Failure(val errorMessage: String) : ResultWrapper<Nothing>()
}
