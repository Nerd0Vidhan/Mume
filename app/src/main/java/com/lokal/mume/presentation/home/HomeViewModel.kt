package com.lokal.mume.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lokal.mume.data.mapper.toSongModel
import com.lokal.mume.data.model.ArtistResult
import com.lokal.mume.data.model.SongResult
import com.lokal.mume.domain.model.SongModel
import com.lokal.mume.domain.repository.NetworkCallRepo
import com.lokal.mume.domain.repository.ResultWrapper
import com.lokal.mume.presentation.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val networkCallRepo: NetworkCallRepo
): ViewModel() {

    private val _artistState =
        MutableStateFlow<UiState<List<ArtistResult>>>(UiState.Nothing)
    val artistState: StateFlow<UiState<List<ArtistResult>>> =
        _artistState.asStateFlow()

    private val _mostPlayedState =
        MutableStateFlow<UiState<List<SongModel>>>(UiState.Nothing)
    val mostPlayedState: StateFlow<UiState<List<SongModel>>> =
        _mostPlayedState.asStateFlow()

    fun getArtist(
        query: String,
        limit: Int
    ) {
        viewModelScope.launch {
            _artistState.value = UiState.Loading

            when (
                val result = networkCallRepo.searchArtist(
                    query = query,
                    limit = limit
                )
            ) {
                is ResultWrapper.Success -> {
                    _artistState.value =
                        UiState.Success(result.value.data.results)
                }

                is ResultWrapper.Failure -> {
                    _artistState.value =
                        UiState.Error(result.errorMessage)
                }
            }
        }
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
