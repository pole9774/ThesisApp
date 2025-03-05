package it.polito.thesisapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import it.polito.thesisapp.navigation.NavigationManager
import it.polito.thesisapp.navigation.Screen
import it.polito.thesisapp.navigation.homeGraph
import it.polito.thesisapp.navigation.profileGraph
import it.polito.thesisapp.navigation.taskGraph
import it.polito.thesisapp.navigation.teamGraph
import it.polito.thesisapp.ui.LocalNavController
import it.polito.thesisapp.ui.LocalNavigationManager
import it.polito.thesisapp.ui.components.AppBottomBar

/**
 * Main screen composable function that sets up the navigation and UI components.
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navigationManager = remember { NavigationManager(navController) }

    CompositionLocalProvider(
        LocalNavController provides navController,
        LocalNavigationManager provides navigationManager
    ) {
        Scaffold(
            bottomBar = { AppBottomBar(navigationManager) },
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.buildHomeRoute(),
                modifier = Modifier.padding(paddingValues)
            ) {
                homeGraph(navigationManager)
                teamGraph(navigationManager)
                profileGraph(navigationManager)
                taskGraph(navigationManager)
            }
        }
    }
}


