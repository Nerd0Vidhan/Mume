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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lokal.mume.dummy.FavoritesDummy
import com.lokal.mume.dummy.PlaylistsDummy
import com.lokal.mume.dummy.SettingsDummy
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
                modifier = modifier, // The padding modifier from RootPlayerHost
                rootNavController = navController, // Pass the top-level controller
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this // 'this' is the AnimatedContentScope from the NavHost
            )
        }
        composable(Screen.Search.route) {
            SearchScreen(
                navController = navController,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this)
        }

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
                animatedVisibilityScope = this, // Correctly refers to AnimatedContentScope
                onCollapse = { navController.popBackStack()?: navController.navigate(BottomNavItem.Home.route) }
            )
        }
    }
}
