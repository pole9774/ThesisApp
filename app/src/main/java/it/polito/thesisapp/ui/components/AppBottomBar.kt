package it.polito.thesisapp.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import it.polito.thesisapp.navigation.NavigationManager
import it.polito.thesisapp.navigation.Screen
import it.polito.thesisapp.ui.LocalNavController


@Composable
fun AppBottomBar(navigationManager: NavigationManager) {
    val navController = LocalNavController.current

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        Screen.screensBottomBar.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(painterResource(screen.icon), contentDescription = null) },
                label = { Text(screen.label) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = { navigationManager.navigateToBottomBarDestination(screen) }
            )
        }
    }
}