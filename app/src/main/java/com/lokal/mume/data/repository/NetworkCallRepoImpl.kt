package com.lokal.mume.data.repository

import android.net.http.HttpException
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lokal.mume.data.Pagination.AlbumPagingSource
import com.lokal.mume.data.Pagination.ArtistPagingSource
import com.lokal.mume.data.Pagination.ArtistSongPagingSource
import com.lokal.mume.data.Pagination.SongPagingSource
import com.lokal.mume.data.database.dao.SongDao
import com.lokal.mume.data.mapper.toDomain
import com.lokal.mume.data.mapper.toEntity
import com.lokal.mume.data.mapper.toSongEntity
import com.lokal.mume.data.mapper.toSongModel
import com.lokal.mume.data.model.ArtistResponse
import com.lokal.mume.data.model.ArtistResult
import com.lokal.mume.data.model.SongResponse
import com.lokal.mume.data.remote.NetworkHelper
import com.lokal.mume.domain.model.SongModel
import com.lokal.mume.domain.repository.NetworkCallRepo
import com.lokal.mume.domain.repository.ResultWrapper
import kotlinx.coroutines.flow.Flow
import com.lokal.mume.data.model.SongResult
import com.lokal.mume.data.model.AlbumResult
import com.lokal.mume.data.model.ArtistDetailsResponse
import com.lokal.mume.data.model.BaseResponse
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

    // In NetworkCallRepoImpl.kt
    override fun getArtistPaging(query: String): Flow<PagingData<ArtistResult>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { ArtistPagingSource(api, query) }
        ).flow
    }

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
    override fun getSongsPaging(query: String,sort: String): Flow<PagingData<SongResult>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { SongPagingSource(api, query, sort) }
        ).flow
    }

    override fun getAlbumsPaging(query: String): Flow<PagingData<AlbumResult>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { AlbumPagingSource(api, query) }
        ).flow
    }

    override fun getArtistSongsPaging(artistName: String): Flow<PagingData<SongResult>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { ArtistSongPagingSource(api, artistName) }
        ).flow
    }
    override suspend fun getArtistDetails(artistId: String): ResultWrapper<BaseResponse<ArtistDetailsResponse>> {
        return try {
            val response = api.getArtistDetails(artistId)
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun addToQueue(song: SongModel, atTop: Boolean) {
        val entity = song.toEntity(isInQueue = true).copy(
            isInQueue = true,
            playedAt = if (atTop) System.currentTimeMillis() - 1000000 else System.currentTimeMillis()
        )
        dao.insert(entity)
    }

    override suspend fun getNextInQueue(): SongModel? {
        return dao.getQueue().firstOrNull()?.toSongModel()
    }

    override suspend fun removeFromQueue(songId: String) {
        dao.removeFromQueue(songId)
    }

    override suspend fun getRandomSongs(): List<SongModel> {
        val response = api.randomSongsByGenre(query = "trending", limit = 10)
        return response.data.results.map { it.toSongModel() }
    }

    override suspend fun updateLastPosition(songId: String, position: Long) {
        dao.updateProgress(
            songId = songId,
            position = position,
            timestamp = System.currentTimeMillis() // Update playedAt so it stays in history
        )
    }
}
