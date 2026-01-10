package com.lokal.mume.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lokal.mume.data.model.ArtistResult
import com.lokal.mume.domain.model.SongModel
import com.lokal.mume.presentation.player.HistoryViewModel
import com.lokal.mume.presentation.player.PlayerViewModel
import com.lokal.mume.presentation.utils.*

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    playerViewModel: PlayerViewModel = hiltViewModel(),
    historyViewModel: HistoryViewModel = hiltViewModel()
) {
    val artistState by viewModel.artistState.collectAsStateWithLifecycle()
    val mostPlayedState by viewModel.mostPlayedState.collectAsStateWithLifecycle()
    val history by historyViewModel.history.collectAsStateWithLifecycle()

    val configuration = LocalConfiguration.current
    val elementSize = (configuration.screenHeightDp / 7).dp

    LaunchedEffect(Unit) {
        historyViewModel.load()
        viewModel.getArtist(query = "a", limit = 10)
        viewModel.getMostPlayed(query = "hindi", limit = 10)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        /* ---------------- Recently Played ---------------- */
        if (history.isNotEmpty()) {
            item {
                SectionWithTitle(
                    title = "Recently Played",
                    onSeeAll = {}
                ) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(history, key = { it.id }) { song ->
                            SquircleShapeContainer(
                                song = song,
                                size = elementSize,
                                cornerRadius = elementSize / 3,
                                modifier = Modifier.clickable {
                                    playerViewModel.play(song)
                                }
                            )
                        }
                    }
                }
            }
        }

        /* ---------------- Artists ---------------- */
        item {
            SectionWithTitle(title = "Artists", onSeeAll = {}) {
                when (artistState) {
                    UiState.Loading -> {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(10) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    CircleShimmerPlaceholder(size = elementSize)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    TextShimmerPlaceholder()
                                }
                            }
                        }
                    }

                    is UiState.Success -> {
                        val artists =
                            (artistState as UiState.Success<List<ArtistResult>>).data

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(artists, key = { it.id }) { artist ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    CircleContainer(
                                        artist = artist,
                                        size = elementSize
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = artist.name,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.width(elementSize)
                                    )
                                }
                            }
                        }
                    }

                    is UiState.Error -> {
                        Text(
                            text = (artistState as UiState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    else -> Unit
                }
            }
        }

        /* ---------------- Most Played ---------------- */
        item {
            SectionWithTitle(title = "Most Played", onSeeAll = {}) {
                when (mostPlayedState) {
                    UiState.Loading -> {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(10) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    SquircleShapePlaceHolder(
                                        size = elementSize,
                                        cornerRadius = elementSize / 3,
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    TextShimmerPlaceholder()
                                }
                            }
                        }
                    }

                    is UiState.Success -> {
                        val songs =
                            (mostPlayedState as UiState.Success<List<SongModel>>).data

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(songs, key = { it.id }) { song ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        playerViewModel.play(song)
                                    }
                                ) {
                                    SquircleShapeContainer(
                                        song = song,
                                        size = elementSize,
                                        cornerRadius = elementSize / 3,
                                        modifier = Modifier.clickable {
                                            playerViewModel.play(song)
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = song.title,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.width(elementSize)
                                    )
                                }
                            }
                        }
                    }

                    is UiState.Error -> {
                        Text(
                            text = (mostPlayedState as UiState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    else -> Unit
                }
            }
        }
    }
}
