package com.lokal.mume.data.repository

import android.net.http.HttpException
import com.lokal.mume.data.database.dao.SongDao
import com.lokal.mume.data.mapper.toDomain
import com.lokal.mume.data.mapper.toEntity
import com.lokal.mume.data.mapper.toSongEntity
import com.lokal.mume.data.mapper.toSongModel
import com.lokal.mume.data.model.ArtistResponse
import com.lokal.mume.data.model.SongResponse
import com.lokal.mume.data.remote.NetworkHelper
import com.lokal.mume.domain.model.SongModel
import com.lokal.mume.domain.repository.NetworkCallRepo
import com.lokal.mume.domain.repository.ResultWrapper
import java.io.IOException
import javax.inject.Inject

class NetworkCallRepoImpl @Inject constructor(
    private val api: NetworkHelper,
    private val dao: SongDao
) : NetworkCallRepo {

    override suspend fun getHome()//: /*HomeData*/
    {
    }

    override suspend fun searchArtist(
        query: String,
        limit: Int
    ): ResultWrapper<ArtistResponse> {
        return try {
            val response = api.searchArtist(query, limit)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun searchSongs(
        query: String,
        limit: Int
    ): ResultWrapper<SongResponse> {
        return try {
            val response = api.randomSongsByGenre(query, limit)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    private fun <T> handleException(e: Exception): ResultWrapper<T> {
        return when (e) {
            is HttpException -> ResultWrapper.Failure("Unable to fetch data")
            is IOException -> ResultWrapper.Failure("Network error")
            else -> ResultWrapper.Failure(e.message ?: "Unknown error")
        }
    }

    override suspend fun saveHistory(song: SongModel) {
        dao.insert(song.toEntity(isInQueue = false))
        dao.trimHistory()
    }

    override suspend fun getQueue(): List<SongModel> =
        dao.getQueue().map { it.toDomain() }

    override suspend fun generateNextSongs(): List<SongModel> {
        val history = dao.lastPlayed()
        val dominantGenre =
            history.groupBy { it.genre }
                .maxByOrNull { it.value.size }
                ?.key ?: "hindi"

        val songs = api.randomSongsByGenre(dominantGenre, 5).data.results


        songs.forEach {
            dao.insert(it.toSongEntity(isInQueue = true))
        }

        return songs.map{songResult -> songResult.toSongModel()}
    }

    override suspend fun getHistory(): List<SongModel> {
        return dao.getHistory().map { it.toDomain() }
    }
}
