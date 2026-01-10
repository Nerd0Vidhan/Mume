package com.lokal.mume.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object Favorites : BottomNavItem("favorites", "Favorites", Icons.Default.FavoriteBorder)
    object Playlists : BottomNavItem("playlists", "Playlists", Icons.Default.List)
    object Settings : BottomNavItem("settings", "Settings", Icons.Default.Settings)

    companion object {
        val entries = listOf(Home, Favorites, Playlists, Settings)
    }
}
