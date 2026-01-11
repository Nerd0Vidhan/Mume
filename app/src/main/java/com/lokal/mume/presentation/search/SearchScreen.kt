package com.lokal.mume.presentation.search

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.lokal.mume.data.mapper.toSongModel
import com.lokal.mume.data.model.AlbumResult
import com.lokal.mume.data.model.ArtistResult
import com.lokal.mume.data.model.SongResult
import com.lokal.mume.presentation.album.AlbumGridItem
import com.lokal.mume.presentation.artist.ArtistListItem
import com.lokal.mume.presentation.player.PlayerViewModel
import com.lokal.mume.presentation.song.SongListItem
import com.lokal.mume.presentation.song.bottomSheet.SongOptionsBottomSheet

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    playerViewModel: PlayerViewModel = hiltViewModel(),
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    var showSheet by remember { mutableStateOf(false) }
    var selectedSongForSheet by remember { mutableStateOf<SongResult?>(null) }

    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()
    val context = LocalContext.current

    val categories = listOf("Songs", "Artists", "Albums", "Folders")
    val albums = remember(searchResults.itemSnapshotList.items, selectedCategory) {
        if (selectedCategory == "Albums") {
            searchResults.itemSnapshotList.items.filterIsInstance<AlbumResult>()
        } else emptyList()
    }

    val albumRows = remember(albums) { albums.chunked(2) }


    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
            TextField(
                value = query,
                onValueChange = viewModel::onQueryChange,
                modifier = Modifier.weight(1f).clip(RoundedCornerShape(24.dp)),
                placeholder = { Text("Search...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                trailingIcon = { if(query.isNotEmpty()) IconButton(onClick = { viewModel.onQueryChange("") }) { Icon(Icons.Default.Close, null) } },
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { viewModel.onCategoryChange(category) },
                    label = { Text(category) },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (query.isEmpty()) {
            EmptySearchResultView() 
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (selectedCategory == "Albums") {
                    items(albumRows) { rowItems ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            rowItems.forEach { album ->
                                Box(modifier = Modifier.weight(1f)) {
                                    AlbumGridItem(album, onClick = {
                                        Toast.makeText(context,"Album Clicked->>${album.name}",Toast.LENGTH_SHORT).show()
                                    })
                                }
                            }
                            if (rowItems.size == 1) Spacer(Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }


                items(searchResults.itemCount) { index ->
                    val item = searchResults[index]
                    when (selectedCategory) {
                        "Songs" -> (item as? SongResult)?.let { song ->
                            SongListItem(song, onPlayClick = { playerViewModel.play(song.toSongModel()) }, onMoreClick = {
                                selectedSongForSheet = song
                                showSheet = true
                            }
                            )
                        }
                        "Artists" -> (item as? ArtistResult)?.let { artist ->
                            ArtistListItem(
                                artist = artist,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope,
                                onClick = {
                                    navController.currentBackStackEntry?.savedStateHandle?.set("artist", artist)
                                    navController.navigate("artist_detail/${artist.id}")
                                }
                            )
                        }
//                        "Albums" -> (item as? AlbumResult)?.let { album ->
//                            AlbumGridItem(album, onClick = { /*navController.navigate("album_detail/${album.id}")*/
//                                Toast.makeText(context,"Album Clicked->>${album.name}",Toast.LENGTH_SHORT).show()
//                            })
//                        }
                    }
                }

                searchResults.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> item { 
                            Box(Modifier.fillParentMaxSize()) { 
                                CircularProgressIndicator(Modifier.align(Alignment.Center)) 
                            } 
                        }
                        loadState.refresh is LoadState.NotLoading && itemCount == 0 -> item { 
                            EmptySearchResultView() 
                        }
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

