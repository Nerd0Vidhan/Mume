package com.lokal.mume.presentation.artist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.QueueMusic
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.PlayCircleFilled
import androidx.compose.material.icons.outlined.QueueMusic
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.lokal.mume.data.mapper.toSongModel
import com.lokal.mume.data.model.ArtistResult
import com.lokal.mume.presentation.home.HomeViewModel
import com.lokal.mume.presentation.player.PlayerViewModel
import com.lokal.mume.presentation.song.bottomSheet.SheetOption
import com.lokal.mume.presentation.utils.CustomHorizontalDivider
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistOptionsBottomSheet(
    artist: ArtistResult,
    onDismiss: () -> Unit,
    playerViewModel: PlayerViewModel = hiltViewModel(), // Inject ViewModel
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val artistSongs = homeViewModel.artistSongPagingFlow.collectAsLazyPagingItems().itemSnapshotList.items
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = SquircleShape(topStart = 28.dp, topEnd = 28.dp, cornerSmoothing = CornerSmoothing.High)
    ) {
        Column(modifier = Modifier.fillMaxWidth().navigationBarsPadding()) {
            // Header Section (Circular)
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = artist.image.lastOrNull()?.url,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(text = artist.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = "1 Album  |  20 Songs", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            CustomHorizontalDivider(alpha = 0.08f)

            val options = listOf(
                SheetOption(Icons.Outlined.PlayCircle, "Play") {
                    playerViewModel.playAll(artistSongs)
                },
                SheetOption(Icons.Outlined.PlayCircleFilled, "Play Next") {
                    artistSongs.firstOrNull()?.let { playerViewModel.playNextInQueue(it.toSongModel()) }
                },
                SheetOption(Icons.Outlined.QueueMusic, "Add to Playing Queue") {
                    playerViewModel.addAllToQueue(artistSongs)
                },
                SheetOption(Icons.Outlined.AddCircleOutline, "Add to Playlist") {},
                SheetOption(Icons.Outlined.Share, "Share") {}
            )

            LazyColumn(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                items(options) { option ->
                    SheetRow(option, onDismiss)
                }
            }
        }
    }
}


@Composable
private fun SheetRow(option: SheetOption, onDismiss: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { option.onClick(); onDismiss() }
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = option.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = option.label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}