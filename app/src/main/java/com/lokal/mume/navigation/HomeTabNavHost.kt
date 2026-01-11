package com.lokal.mume.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lokal.mume.dummy.CenterText
import com.lokal.mume.presentation.home.HomeScreenContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import com.lokal.mume.presentation.album.AlbumListScreen
import com.lokal.mume.presentation.artist.ArtistListScreen // For the Artists Tab
import com.lokal.mume.presentation.song.SongsListScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeTabNavHost(
    navController: NavHostController,
    rootNavController: NavHostController, // Used to navigate to FullPlayer or ArtistDetail
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "suggested",
        modifier = modifier
    ) {
        // Tab 1: Suggested (Your Home Dashboard)
        composable("suggested") {
            HomeScreenContent(
                navController = rootNavController,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }

        composable("songs") {
            SongsListScreen(

            )
        }

        // Tab 3: Artists (Paginated Grid/List of Artists)
        composable("artists") {
            ArtistListScreen(
                navController = rootNavController,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }

        composable("albums") {
            AlbumListScreen(navController = rootNavController)
        }
        composable("folders") { folders() }
    }
}

@Composable fun folders() = CenterText("folders")