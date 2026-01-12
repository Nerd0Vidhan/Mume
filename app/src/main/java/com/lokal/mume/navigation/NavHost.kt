package com.lokal.mume.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lokal.mume.data.model.ArtistResult
import com.lokal.mume.dummy.FavoritesDummy
import com.lokal.mume.dummy.PlaylistsDummy
import com.lokal.mume.dummy.SettingsDummy
import com.lokal.mume.presentation.artist.ArtistDetailScreen
import com.lokal.mume.presentation.player.ui.FullPlayerScreen
import com.lokal.mume.presentation.search.SearchScreen
import com.lokal.mume.presentation.topBar.HomeRootScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MumeNavGraph(
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        route = "root_graph",
        startDestination = BottomNavItem.Home.route
    ) {
        composable(BottomNavItem.Home.route) {
            HomeRootScreen(
                modifier = modifier,
                rootNavController = navController,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this
            )
        }
        composable(Screen.Search.route) {
            SearchScreen(
                navController = navController,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this)
        }


        composable(
            route = "artist_detail/{artistId}",
            // Define arguments for better performance
            arguments = listOf(navArgument("artistId") { type = NavType.StringType })
        ) { backStackEntry ->
            val artist = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<ArtistResult>("artist")

            if (artist != null) {
                ArtistDetailScreen(
                    artist = artist,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = this, // Pass the scope of this destination
                    onBack = { navController.popBackStack() },
                    navController = navController
                )
            }
        }
        composable("album_detail/{albumId}") {  }

        composable(BottomNavItem.Favorites.route) { FavoritesDummy() }
        composable(BottomNavItem.Playlists.route) { PlaylistsDummy() }
        composable(BottomNavItem.Settings.route) { SettingsDummy() }

        // In MumeNavGraph.kt
        // In MumeNavGraph.kt
        composable(
            route = Screen.FullScreen.route,
            // Keep these empty or very subtle so they don't fight with sharedBounds
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) }
        ) {
            FullPlayerScreen(
                navController = navController,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this,
                onCollapse = { navController.popBackStack() }
            )
        }
    }
}
