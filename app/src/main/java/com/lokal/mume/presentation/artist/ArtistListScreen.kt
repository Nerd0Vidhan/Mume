package com.lokal.mume.presentation.artist

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.lokal.mume.presentation.home.HomeViewModel
import com.lokal.mume.presentation.utils.CustomHorizontalDivider

@Composable
fun ArtistListScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavHostController
) {
    val artists = viewModel.artistPagingFlow.collectAsLazyPagingItems()

    Column(Modifier.fillMaxSize()) {
        // Section Header (Image 12)
        Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("${artists.itemCount} artists", fontWeight = FontWeight.Bold)
            Text("Date Added ↑↓", color = MaterialTheme.colorScheme.primary)
        }
        CustomHorizontalDivider(thickness = 1.dp)

        LazyColumn {
            items(
                count = artists.itemCount,
                key = artists.itemKey { it.id }
            ) { index ->
                artists[index]?.let { artist ->
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
            }
            if (artists.loadState.append is LoadState.Loading) {
                item {
                    ArtistItemShimmer()
                }
            }
        }
    }
}