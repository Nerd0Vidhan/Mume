package com.lokal.mume.navigation

sealed class Screen(val route: String) {
    // Main Root Screens
    object Home : Screen("home_root")
    object Search : Screen("search")
    object Favorites : Screen("Favorites")
    object Playlist : Screen("playlist")
    object Settings : Screen("settings")

    // Top Tabs for the "Home" Bottom Nav option
    object Suggested : Screen("suggested")
    object Songs : Screen("songs")
    object Artist : Screen("artist")
    object Album : Screen("album")
    object Folder : Screen("folder")

    // Full Screen Player
    object FullScreen :Screen("player_full")
}