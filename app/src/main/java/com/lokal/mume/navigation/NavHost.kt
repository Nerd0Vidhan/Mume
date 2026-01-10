package com.lokal.mume.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lokal.mume.dummy.FavoritesDummy
import com.lokal.mume.dummy.HomeDummy
import com.lokal.mume.dummy.PlaylistsDummy
import com.lokal.mume.dummy.SettingsDummy
import com.lokal.mume.presentation.player.ui.FullPlayerScreen
import com.lokal.mume.presentation.topBar.HomeScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MumeNavGraph(
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope
){
    NavHost(
        navController = navController,
        route = "root_graph",
        startDestination = BottomNavItem.Home.route
    ) {

        composable(BottomNavItem.Home.route) { HomeScreen() }
        composable(BottomNavItem.Favorites.route) { FavoritesDummy() }
        composable(BottomNavItem.Playlists.route) { PlaylistsDummy() }
        composable(BottomNavItem.Settings.route) { SettingsDummy() }


        composable(Screen.FullScreen.route) {
            FullPlayerScreen(
                navController = navController,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this,
                onCollapse = { navController.popBackStack() }
            )
        }
    }
}
