package com.lokal.mume.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.lokal.mume.domain.repository.NetworkCallRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: NetworkCallRepo
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Songs")
    val selectedCategory = _selectedCategory.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults = combine(_searchQuery, _selectedCategory) { query, category ->
        Pair(query, category)
    }.flatMapLatest { (query, category) ->
        if (query.isEmpty()) {
            flowOf(PagingData.empty<Any>())
        } else {
            when (category) {
                "Songs" -> repo.getSongsPaging(query, sort = "asc").map { pagingData -> pagingData.map { it as Any } }
                "Artists" -> repo.getArtistPaging(query).map { pagingData -> pagingData.map { it as Any } }
                "Albums" -> repo.getAlbumsPaging(query).map { pagingData -> pagingData.map { it as Any } }
                else -> flowOf(PagingData.empty<Any>())
            }
        }
    }.cachedIn(viewModelScope)

    fun onQueryChange(newQuery: String) { _searchQuery.value = newQuery }
    fun onCategoryChange(category: String) { _selectedCategory.value = category }
}
