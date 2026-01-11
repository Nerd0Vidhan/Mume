package com.lokal.mume.data.Pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lokal.mume.data.model.AlbumResult // Ensure you have an AlbumResult model
import com.lokal.mume.data.remote.NetworkHelper

class AlbumPagingSource(
    private val api: NetworkHelper,
    private val query: String
) : PagingSource<Int, AlbumResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AlbumResult> {
        return try {
            val currentPage = params.key ?: 1
            // Most Saavn APIs use 'search/albums' or 'albums' for trending
            val response = api.searchAlbums(query = query, page = currentPage, limit = params.loadSize)
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

    override fun getRefreshKey(state: PagingState<Int, AlbumResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}