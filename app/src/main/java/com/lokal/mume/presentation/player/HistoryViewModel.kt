package com.lokal.mume.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lokal.mume.domain.model.SongModel
import com.lokal.mume.domain.repository.NetworkCallRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repo: NetworkCallRepo
) : ViewModel() {

    val history = MutableStateFlow<List<SongModel>>(emptyList())

    fun load() {
        viewModelScope.launch {
            history.value = repo.getHistory()
        }
    }
}

