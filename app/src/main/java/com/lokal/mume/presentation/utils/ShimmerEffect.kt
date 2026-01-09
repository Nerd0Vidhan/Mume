package com.lokal.mume.presentation.utils

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.RectangleShape

fun Modifier.shimmer(
    shape: Shape = RectangleShape,
    durationMillis: Int = 1000
): Modifier = composed {

    var size by remember { mutableStateOf(IntSize.Zero) }

    val transition = rememberInfiniteTransition(label = "shimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val shimmerColor = MaterialTheme.colorScheme.onSurfaceVariant

    val brush = Brush.linearGradient(
        colors = listOf(
            shimmerColor.copy(alpha = 0.3f),
            shimmerColor.copy(alpha = 0.7f),
            shimmerColor.copy(alpha = 0.3f)
        ),
        start = Offset(
            x = size.width * translateAnim,
            y = 0f
        ),
        end = Offset(
            x = size.width * (translateAnim + 1f),
            y = size.height.toFloat()
        )
    )

    this
        .onGloballyPositioned { size = it.size }
        .background(brush = brush, shape = shape)
}
