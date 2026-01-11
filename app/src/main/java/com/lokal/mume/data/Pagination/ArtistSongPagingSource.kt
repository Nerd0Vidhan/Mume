package com.lokal.mume.data.Pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lokal.mume.data.model.ArtistResult
import com.lokal.mume.data.model.SongResult
import com.lokal.mume.data.remote.NetworkHelper

class ArtistSongPagingSource(
    private val api: NetworkHelper,
    private val artistName: String
) : PagingSource<Int, SongResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SongResult> {
        return try {
            val currentPage = params.key ?: 1
            val response = api.searchSongs(query = artistName, page = currentPage, limit = params.loadSize)
            val results = response.data.results

            LoadResult.Page(
                data = results,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (results.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SongResult>): Int? = state.anchorPosition
}