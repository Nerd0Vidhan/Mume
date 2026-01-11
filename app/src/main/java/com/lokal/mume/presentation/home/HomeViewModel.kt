package com.lokal.mume.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.lokal.mume.data.mapper.toSongModel
import com.lokal.mume.data.model.ArtistResult
import com.lokal.mume.data.model.SongResult
import com.lokal.mume.domain.model.SongModel
import com.lokal.mume.domain.repository.NetworkCallRepo
import com.lokal.mume.domain.repository.ResultWrapper
import com.lokal.mume.presentation.sort.SortType
import com.lokal.mume.presentation.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val networkCallRepo: NetworkCallRepo
): ViewModel() {

    // Standard State for non-paginated sections
    private val _mostPlayedState = MutableStateFlow<UiState<List<SongModel>>>(UiState.Nothing)
    val mostPlayedState = _mostPlayedState.asStateFlow()

    // Paging Flow for Artists
    val artistPagingFlow = networkCallRepo.getArtistPaging("b")
        .cachedIn(viewModelScope)

//    // In HomeViewModel.kt
//    val songPagingFlow = networkCallRepo.getSongsPaging("trending") // Or a dynamic query
//        .cachedIn(viewModelScope)

    val albumPagingFlow = networkCallRepo.getAlbumsPaging("latest")
        .cachedIn(viewModelScope)

    private val _currentSort = MutableStateFlow(SortType("Ascending", "asc"))
    val currentSort = _currentSort.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val songPagingFlow = _currentSort.flatMapLatest { sort ->
        networkCallRepo.getSongsPaging(query = "trending", sort = sort.value)
        // We pass the sort value to the repository
    }.cachedIn(viewModelScope)

    fun updateSort(newSort: SortType) {
        _currentSort.value = newSort
    }

    init {
        getMostPlayed("hindi", 10)
    }

    fun getMostPlayed(query: String = "hindi", limit: Int = 10) {
        viewModelScope.launch {
            _mostPlayedState.value = UiState.Loading
            when (val result = networkCallRepo.searchSongs(query, limit)) {
                is ResultWrapper.Success -> {
                    _mostPlayedState.value = UiState.Success(
                        result.value.data.results.map { it.toSongModel() }
                    )
                }
                is ResultWrapper.Failure -> {
                    _mostPlayedState.value = UiState.Error(result.errorMessage)
                }
            }
        }
    }
}