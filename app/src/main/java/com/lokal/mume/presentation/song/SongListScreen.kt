package com.lokal.mume.presentation.song

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.lokal.mume.data.mapper.toSongModel
import com.lokal.mume.data.model.SongResult
import com.lokal.mume.presentation.home.HomeViewModel
import com.lokal.mume.presentation.player.PlayerViewModel
import com.lokal.mume.presentation.song.bottomSheet.SongOptionsBottomSheet
import com.lokal.mume.presentation.sort.SortPopUp
import com.lokal.mume.presentation.utils.CircleShimmerPlaceholder
import com.lokal.mume.presentation.utils.CustomHorizontalDivider

@Composable
fun SongsListScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    val songs = viewModel.songPagingFlow.collectAsLazyPagingItems()
    val selectedSort by viewModel.currentSort.collectAsState()

    var showSheet by remember { mutableStateOf(false) }
    var selectedSongForSheet by remember { mutableStateOf<SongResult?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
        // Song Count and Sort Header (Image 6)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${songs.itemCount} songs", // Ideally dynamic: "${songs.itemCount} songs"
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            SortPopUp(
                currentSort = selectedSort,
                onSortSelected = { newSort ->
                    viewModel.updateSort(newSort)
                }
            )
        }

        CustomHorizontalDivider(alpha = 0.1f)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp) // Space for MiniPlayer
        ) {
            items(
                count = songs.itemCount,
                key = songs.itemKey { it.id }
            ) { index ->
                val song = songs[index]
                if (song != null) {
                    SongListItem(
                        song = song,
                        onPlayClick = { playerViewModel.play(song.toSongModel()) },
                        onMoreClick = {
                            selectedSongForSheet = song
                            showSheet = true
                                      }
                    )
                }
            }


            // Paging Load States
            songs.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        items(10) { CircleShimmerPlaceholder(30.dp) }
                    }
                    loadState.append is LoadState.Loading -> {
                        item { CircularProgressIndicator(Modifier.fillMaxWidth().padding(16.dp)) }
                    }
                }
            }
        }
        if (showSheet && selectedSongForSheet != null) {
            SongOptionsBottomSheet(
                song = selectedSongForSheet!!.toSongModel(),
                onDismiss = { showSheet = false },
                onFavoriteClick = { /* Handle Favorite Logic */ }
            )
        }
    }
}