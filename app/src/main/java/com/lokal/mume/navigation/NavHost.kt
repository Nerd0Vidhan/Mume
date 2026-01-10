package com.lokal.mume.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) { 
            HomeScreen() 
        }
        
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
