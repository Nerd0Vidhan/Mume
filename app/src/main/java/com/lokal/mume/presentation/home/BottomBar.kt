package com.lokal.mume.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lokal.mume.navigation.BottomNavItem
import com.lokal.mume.navigation.Screen
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape

@Composable
fun BottomBar(
    navController: NavController,
    isClippedEdge: Boolean? = true
) {
    val bottomBarShape = SquircleShape(
        topStart = 40.dp,
        topEnd = 40.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp,
        cornerSmoothing = CornerSmoothing.High
    )

    // Using a lower alpha for that "behind the bar" glass look
    val backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)

    // Outer container: Handles the background color that fills the navigation bar area
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = if (isClippedEdge == true) bottomBarShape else RectangleShape
            )
    ) {
        // Inner container: Handles the actual layout and pushes content above system bars
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding() // Pushes icons above the system nav bar
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val items = listOf(
                Triple(Icons.Default.Home, "Home", BottomNavItem.Home.route),
                Triple(Icons.Default.FavoriteBorder, "Favorites", BottomNavItem.Favorites.route),
                Triple(Icons.Default.List, "Playlist", BottomNavItem.Playlists.route),
                Triple(Icons.Default.Settings, "Settings", BottomNavItem.Settings.route)
            )

            items.forEach { (icon, label, route) ->
                BottomBarItem(
                    icon = icon,
                    label = label,
                    route = route,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun BottomBarItem(
    icon: ImageVector,
    label: String,
    route: String,
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isSelected = currentRoute == route

    val color = if (isSelected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onSurface

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        IconButton(onClick = {
            if (currentRoute != route) {
                navController.navigate(route) {
                    // Clear backstack to the start destination
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}
