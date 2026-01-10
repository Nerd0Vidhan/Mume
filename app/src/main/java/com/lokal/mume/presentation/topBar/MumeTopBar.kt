package com.lokal.mume.presentation.topBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.lokal.mume.presentation.home.BottomBar
import com.lokal.mume.presentation.home.HomeScreenContent

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
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // ✅ ONLY padding
        ){
//            HomeNavGraph(navController = bottomNavController)
            HomeScreenContent()
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
