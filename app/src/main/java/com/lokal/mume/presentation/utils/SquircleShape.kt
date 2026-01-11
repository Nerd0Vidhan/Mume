package com.lokal.mume.presentation.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lokal.mume.data.model.ArtistResult
import com.lokal.mume.domain.model.SongModel
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape

@Composable
fun SquircleShapeContainer(
    song: SongModel? = null,
    artistResult: ArtistResult? =null,
    size: Dp,
    cornerRadius: Dp,
    modifier: Modifier = Modifier,
) {
    val squircle = SquircleShape(
        radius = cornerRadius,
        cornerSmoothing = CornerSmoothing.High
    )
    AsyncImage(
        model = song?.artwork ?: artistResult?.image?.lastOrNull()?.url,//artist.image.lastOrNull()?.url
        contentDescription = "Squircle Container",
        modifier = modifier
            .size(size)
            .clip(
                shape = squircle
            )
    )
}

@Composable
fun SquircleShapePlaceHolder(size: Dp, cornerRadius: Dp ){
    val squircle = SquircleShape(
        radius = cornerRadius,
        cornerSmoothing = CornerSmoothing.High
    )

    Box(
        modifier = Modifier
            .size(size)
            .shimmer(shape = squircle)
    )

}
