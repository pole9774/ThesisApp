package it.polito.thesisapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
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
import it.polito.thesisapp.ui.components.MainFab
import it.polito.thesisapp.viewmodel.CreateTaskViewModel
import it.polito.thesisapp.viewmodel.CreateTeamViewModel
import it.polito.thesisapp.viewmodel.MainScreenViewModel

/**
 * Composable function that displays the main screen of the application.
 * It sets up the navigation host and the bottom navigation bar.
 */
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    createTeamViewModel: CreateTeamViewModel = hiltViewModel(),
    createTaskViewModel: CreateTaskViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val navigationManager = remember { NavigationManager(navController) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    CompositionLocalProvider(
        LocalNavController provides navController,
        LocalNavigationManager provides navigationManager
    ) {
        Scaffold(
            bottomBar = { AppBottomBar(navigationManager) },
            floatingActionButton = {
                MainFab(
                    currentRoute = currentRoute,
                    onFabClick = {
                        viewModel.handleFabAction(
                            navigationManager = navigationManager,
                            currentRoute = currentRoute,
                            createTeamAction = { name, description, onSuccess ->
                                createTeamViewModel.submitTeamAndNavigate(name, description, onSuccess)
                            },
                            createTaskAction = { teamId, name, description ->
                                createTaskViewModel.createTask(teamId, name, description)
                            }
                        )
                    }
                )
            }
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


