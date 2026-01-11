package com.lokal.mume.data.Pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lokal.mume.data.model.SongResult
import com.lokal.mume.data.remote.NetworkHelper

class SongPagingSource(
    private val api: NetworkHelper,
    private val query: String,
    private val sort: String = "asc"
) : PagingSource<Int, SongResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SongResult> {
        return try {
            val currentPage = params.key ?: 1
            // Fetching from the search/songs endpoint
            val response = api.searchSongs(query = query, page = currentPage, limit = params.loadSize,sort = sort)
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

    override fun getRefreshKey(state: PagingState<Int, SongResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}