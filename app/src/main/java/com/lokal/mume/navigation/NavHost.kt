package com.lokal.mume.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lokal.mume.presentation.topBar.HomeScreen

@Composable
fun MumeNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        route = "root_graph",
        startDestination = Screen.Home.route // Set starting destination
    ) {
        // Simple screens
        composable(Screen.Home.route) { HomeScreen() }

        // The Search screen is a separate screen outside the bottom bar
//        composable(Screen.Search.route) { SearchScreen(navController) }
//
//        // The Home Screen which contains the Bottom Bar and its own NavHost
//        composable(Screen.Home.route) {
//            MainHomeScreen()
//        }
    }
}