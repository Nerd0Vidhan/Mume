package com.lokal.mume.presentation.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape

@Composable
fun SquircleShapeContainer(image: Painter, size: Dp, cornerRadius: Dp ){
    val squircle = SquircleShape(
        radius = cornerRadius,
        cornerSmoothing = CornerSmoothing.High
    )
    Image(
        painter = image,
        contentDescription = "Squircle Container",
        modifier = Modifier
            .size(size)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary, // High visibility for debugging
                shape = squircle
            )
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