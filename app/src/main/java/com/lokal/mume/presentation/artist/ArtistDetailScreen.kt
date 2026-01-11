package com.lokal.mume.presentation.artist

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionDefaults
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.lokal.mume.data.mapper.toSongModel
import com.lokal.mume.data.model.ArtistResult
import com.lokal.mume.data.model.SongResult
import com.lokal.mume.presentation.home.HomeViewModel
import com.lokal.mume.presentation.player.PlayerViewModel
import com.lokal.mume.presentation.song.SongListItem
import com.lokal.mume.presentation.song.bottomSheet.SongOptionsBottomSheet
import com.lokal.mume.presentation.utils.SquircleShapeContainer
import com.lokal.mume.presentation.utils.UiState
import com.lokal.mume.presentation.utils.formatTime


// com.lokal.mume.presentation.artist.ArtistDetailScreen.kt
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ArtistDetailScreen(
    artist: ArtistResult,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: HomeViewModel = hiltViewModel(),
    playerViewModel: PlayerViewModel = hiltViewModel(),
    onBack: () -> Unit,
    navController: NavController
) {
    var showArtistOptions by remember { mutableStateOf(false) }

    val detailsState by viewModel.artistDetails.collectAsState()
    LaunchedEffect(artist.name) {
        viewModel.getArtistSongs(artist.name)
    }

    LaunchedEffect(artist.id) {
        viewModel.fetchArtistDetails(artist.id)
    }

    val songs = viewModel.artistSongPagingFlow.collectAsLazyPagingItems()
    var showSheet by remember { mutableStateOf(false) }
    var selectedSongForSheet by remember { mutableStateOf<SongResult?>(null) }


    with(sharedTransitionScope) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                ArtistDetailTopBar(
                    onBack = onBack,
                    navController = navController,
                    onMore= {
                        showArtistOptions = true
                    }
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // Artist Profile Section
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))

                        // Squircle Image Morphing
                        SquircleShapeContainer(
                            artistResult = artist,
                            size = 200.dp,
                            cornerRadius = 100.dp,
                            modifier = Modifier.sharedBounds(
                                rememberSharedContentState(key = "artist_image_${artist.id}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = SharedTransitionDefaults.BoundsTransform,
                                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(contentScale = ContentScale.FillBounds)
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = artist.name,
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState(key = "artist_name_${artist.id}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        )
                        when (val state = detailsState) {
                            is UiState.Success -> {
                                val data = state.data
                                Text(
                                    text = "${data.albumCount ?: 0} Album | ${data.songCount ?: 0} Songs | ${data.topSongs.sumOf { it.duration.toLong() }.formatTime()} Top Songs",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            is UiState.Loading -> {
                                // Show a small shimmer or placeholder
                            }
                            else -> { /* Handle error/default */ }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Radial/Shadow Styled Buttons
                        ArtistActionButtons(
                            onShuffle = {
                                val currentSongs = songs.itemSnapshotList.items
                                playerViewModel.playRandom(songs = currentSongs) },
                            onPlay ={
                                val currentSongs = songs.itemSnapshotList.items
                                playerViewModel.addAllToQueue(songs=currentSongs)
                                playerViewModel.playAll(currentSongs)
                            }
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }

                // Songs Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Songs", style = MaterialTheme.typography.titleLarge)
                        Text("See All", color = MaterialTheme.colorScheme.primary)
                    }
                }

                // Paginated Song List
                items(
                    count = songs.itemCount,
                    key ={ index ->
                        val song = songs.peek(index)
                        "${song?.id ?: index}_$index"
                    }
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
            }
            if (showArtistOptions) {
                ArtistOptionsBottomSheet(
                    artist = artist,
                    onDismiss = { showArtistOptions = false }
                )
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
}