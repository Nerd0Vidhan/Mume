package com.lokal.mume.domain.repository

import com.lokal.mume.data.model.ArtistResponse

interface NetworkCallRepo {
    suspend fun getHome()//: /*HomeData*/

    suspend fun searchArtist(query: String, limit: Int): ResultWrapper<ArtistResponse>
}


sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Failure(val errorMessage: String) : ResultWrapper<Nothing>()
}
