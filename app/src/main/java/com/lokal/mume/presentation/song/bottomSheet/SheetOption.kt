package com.lokal.mume.presentation.song.bottomSheet

import androidx.compose.ui.graphics.vector.ImageVector

data class SheetOption(
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit
)