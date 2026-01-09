package com.lokal.mume.presentation.utils

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lokal.mume.data.model.ArtistResult

@Composable
fun CircleContainer(artist: ArtistResult, size: Dp) {
    AsyncImage(
        model = artist.image.lastOrNull()?.url,
        contentDescription = "Circle Container",
        modifier = Modifier
            .size(size)
            .clip(shape = CircleShape)
    )
}

@Composable
fun CircleShimmerPlaceholder(size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .shimmer(shape = CircleShape)
    )
}
