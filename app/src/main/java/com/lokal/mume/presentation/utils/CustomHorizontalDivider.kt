package com.lokal.mume.presentation.utils

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    alpha: Float = 0.12f // Subtle transparency for a clean look
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha)
    )
}