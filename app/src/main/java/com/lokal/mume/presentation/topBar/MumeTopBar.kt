package com.lokal.mume.presentation.topBar

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lokal.mume.navigation.HomeTabNavHost

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeRootScreen(
    modifier: Modifier = Modifier,
    rootNavController: NavHostController, // The root controller for app-level navigation
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val subNavController = rememberNavController()
    val navBackStackEntry by subNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "suggested"

    val tabs = listOf("suggested", "songs", "artists", "albums", "folders")
    val selectedIndex = tabs.indexOf(currentRoute).coerceAtLeast(0)

    Column(modifier = modifier.fillMaxSize()) {
        HomeTopBarTabs(
            selectedTabIndex = selectedIndex,
            onTabSelected = { index ->
                subNavController.navigate(tabs[index]) {
                    popUpTo(subNavController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        HomeTabNavHost(
            navController = subNavController,
            rootNavController = rootNavController, // Use this to navigate to Artist Details/Full Player
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = Modifier.weight(1f)
        )
    }
}