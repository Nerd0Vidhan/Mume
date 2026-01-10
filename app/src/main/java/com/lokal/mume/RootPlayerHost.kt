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
import com.lokal.mume.navigation.MumeNavGraph
import com.lokal.mume.navigation.Screen
import com.lokal.mume.presentation.home.BottomBar
import com.lokal.mume.presentation.player.PlayerViewModel
import com.lokal.mume.presentation.player.ui.FullPlayerScreen
import com.lokal.mume.presentation.player.ui.MiniPlayer

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RootPlayerHost(navController: NavHostController) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val state by playerViewModel.playbackState.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Hide bottom bar and mini player on full screen player
    val isFullScreenPlayer = currentRoute == Screen.FullScreen.route

    SharedTransitionLayout {
        Scaffold(
            bottomBar = {
                if (!isFullScreenPlayer) {
                    BottomBar(
                        navController = navController,
                        isClippedEdge = state.currentSong == null,
                    )
                }
            }
        ) { padding ->

// Inside RootPlayerHost...

            Box(Modifier.fillMaxSize()) {
                MumeNavGraph(
                    navController = navController,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
                AnimatedContent(
                    targetState = (!isFullScreenPlayer && state.currentSong != null),
                    transitionSpec = {
                        (slideInVertically { it } + fadeIn())
                            .togetherWith(slideOutVertically { it } + fadeOut())
                    },
                    label = "PlayerTransition",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(padding)
                ) { showMiniPlayer ->
                    if (showMiniPlayer) {
                        MiniPlayer(
                            state = state,
                            onExpand = {
                                navController.navigate(Screen.FullScreen.route) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this,
                            playerViewModel = playerViewModel
                        )
                    } else if (isFullScreenPlayer) {
                        FullPlayerScreen(
                            navController = navController,
                            viewModel = playerViewModel,
                            onCollapse = { navController.popBackStack() },
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this
                        )
                    }
                }
            }
        }
    }
}
