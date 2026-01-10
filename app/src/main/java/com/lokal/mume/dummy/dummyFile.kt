package com.lokal.mume.dummy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeDummy() = CenterText("Home")
@Composable fun FavoritesDummy() = CenterText("Favorites")
@Composable fun PlaylistsDummy() = CenterText("Playlists")
@Composable fun SettingsDummy() = CenterText("Settings")

@Composable
private fun CenterText(text: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text)
    }
}
