package com.lokal.mume.data.Pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lokal.mume.data.model.ArtistResult
import com.lokal.mume.data.remote.NetworkHelper

class ArtistPagingSource(
    private val api: NetworkHelper,
    private val query: String
) : PagingSource<Int, ArtistResult>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtistResult> {
        return try {
            val position = params.key ?: 1
            val response = api.searchArtist(query, position, params.loadSize)
            val results = response.data.results

            LoadResult.Page(
                data = results,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (results.isEmpty()) null else position + 1
            )
        } catch (e: Exception) { LoadResult.Error(e) }
    }
    override fun getRefreshKey(state: PagingState<Int, ArtistResult>): Int? = state.anchorPosition
}