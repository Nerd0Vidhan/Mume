package com.lokal.mume.presentation.topBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.lokal.mume.presentation.home.BottomBar
import com.lokal.mume.presentation.home.HomeScreen

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val bottomNavController = rememberNavController()
    Scaffold(
        topBar = {
            TopBarHeader()
        },
        bottomBar = {
            BottomBar(bottomNavController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)
            .verticalScroll(rememberScrollState())) {
//            HomeNavGraph(navController = bottomNavController)
            HomeScreen()
        }
    }
//    Column(
//        modifier = modifier
//            .fillMaxWidth()
//            .background(MaterialTheme.colorScheme.background)
//            .statusBarsPadding()
//    ) {
//        TopBarHeader()
//        TopBarTabs()
//        HomeScreen()
//        BottomBar(rememberNavController())
//    }
}
