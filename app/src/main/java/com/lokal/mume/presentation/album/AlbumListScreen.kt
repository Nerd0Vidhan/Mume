package com.lokal.mume.presentation.album

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
fun AlbumListScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    val albums = viewModel.albumPagingFlow.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
        // Header Section (Image 13)
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${albums.itemCount} albums",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Date Modified ↑↓",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge
            )
        }
        CustomHorizontalDivider(thickness = 1.dp)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = albums.itemCount,
                key = albums.itemKey { it.id }
            ) { index ->
                albums[index]?.let { album ->
                    AlbumGridItem(
                        album = album,
                        onClick = { /*navController.navigate("album_detail/${album.id}")*/
                            Toast.makeText(context,"Album Clicked->>${album.name}",Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
            if (albums.loadState.append is LoadState.Loading) {
                items(2) {
                    AlbumShimmerItem()
                }
            }
        }
    }
}