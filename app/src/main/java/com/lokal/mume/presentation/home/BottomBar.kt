package com.lokal.mume.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.lokal.mume.navigation.Screen
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape

@Composable
fun BottomBar(
    navController: NavController,
    isClippedEdge: Boolean? = true
) {
    // Define our custom shape and transparent color
    val bottomBarShape = SquircleShape(
        topStart = 40.dp,
        topEnd = 40.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp,
        cornerSmoothing = CornerSmoothing.High
    )
    val backgroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            // Applying background with the shape handles both clipping and coloring
            .background(
                color = backgroundColor,
                shape = if (isClippedEdge == true) bottomBarShape else RectangleShape
            )
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Home / TabOne Option
        BottomBarItem(
            icon = Icons.Default.Home,
            route = Screen.Home.route,
            navController = navController
        )

        // Library Option
        BottomBarItem(
            icon = Icons.Default.FavoriteBorder,
            route = Screen.Favorites.route,
            navController = navController
        )

        // Profile Option
        BottomBarItem(
            icon = Icons.Default.List,
            route = Screen.Playlist.route,
            navController = navController
        )

        // Settings Option
        BottomBarItem(
            icon = Icons.Default.Settings,
            route = Screen.Settings.route,
            navController = navController
        )
    }
}

@Composable
fun BottomBarItem(
    icon: ImageVector,
    route: String,
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isSelected = currentRoute == route

    IconButton(onClick = {
        if (currentRoute != route) {
            navController.navigate(route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }
    }) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            // Highlight the icon if it's the active route
            tint = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
@Preview(showBackground = true)
@Composable
fun prev(){
    BottomBar(rememberNavController())
}
