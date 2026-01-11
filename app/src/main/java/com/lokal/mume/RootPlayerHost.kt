package com.lokal.mume

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lokal.mume.navigation.BottomNavItem
import com.lokal.mume.navigation.MumeNavGraph
import com.lokal.mume.navigation.Screen
import com.lokal.mume.presentation.home.BottomBar
import com.lokal.mume.presentation.player.PlayerViewModel
import com.lokal.mume.presentation.player.ui.MiniPlayer
import com.lokal.mume.presentation.topBar.TopBarHeader

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RootPlayerHost(navController: NavHostController) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val state by playerViewModel.playbackState.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isFullScreenComposable = currentRoute == Screen.FullScreen.route
            || currentRoute == Screen.Search.route
            || currentRoute == "artist_detail/{artistId}"



    SharedTransitionLayout {
        Scaffold(
            topBar = {
                AnimatedVisibility(
                    visible = !isFullScreenComposable,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) { TopBarHeader(navController) }
            },
            bottomBar = {
                // Use AnimatedVisibility instead of 'if' to allow Shared Elements to finish
                AnimatedVisibility(
                    visible = !isFullScreenComposable,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    // Grouping ensures they slide together as a single unit
                    Column {
                        AnimatedContent(
                            targetState = state.currentSong != null,
                            label = "MiniPlayer"
                        ) { hasSong ->
                            if (hasSong) {
                                MiniPlayer(
                                    state = state,
                                    onExpand = { navController.navigate(Screen.FullScreen.route) },
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@AnimatedVisibility, // Use Scaffold's scope
                                    playerViewModel = playerViewModel,
                                    onDismiss = { playerViewModel.pause() }
                                )
                            }
                        }
                        BottomBar(
                            navController = navController,
                            isClippedEdge = state.currentSong == null,
                        )
                    }
                }
            }
        ) { padding ->
            MumeNavGraph(
                navController = navController,
                sharedTransitionScope = this@SharedTransitionLayout,
                modifier = Modifier.padding(padding)
            )
        }
    }
}