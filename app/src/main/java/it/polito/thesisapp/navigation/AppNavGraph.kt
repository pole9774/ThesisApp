package it.polito.thesisapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import it.polito.thesisapp.navigation.NavigationManager.NavigationEvent
import it.polito.thesisapp.ui.screens.CreateTaskScreen
import it.polito.thesisapp.ui.screens.CreateTeamScreen
import it.polito.thesisapp.ui.screens.HomeScreen
import it.polito.thesisapp.ui.screens.ProfileScreen
import it.polito.thesisapp.ui.screens.TaskScreen
import it.polito.thesisapp.ui.screens.TeamScreen
import it.polito.thesisapp.utils.Constants

/**
 * Adds the home navigation graph to the NavGraphBuilder.
 * Sets up routes for home and create team screens.
 *
 * @param navigationManager The navigation manager used for navigation events
 */
fun NavGraphBuilder.homeGraph(
    navigationManager: NavigationManager,
) {
    composable(
        route = Screen.buildHomeRoute()
    ) {
        HomeScreen(
            userId = Constants.User.USER_ID,
            onNavigateToTeam = { teamId ->
                navigationManager.navigate(NavigationEvent.NavigateToTeam(teamId))
            },
            onNavigateToTask = { teamId, taskId ->
                navigationManager.navigate(NavigationEvent.NavigateToTask(teamId, taskId))
            }
        )
    }
    composable(
        route = Screen.buildCreateTeamRoute()
    ) {
        CreateTeamScreen(
            navigationManager = navigationManager,
            afterTeamCreated = {
                navigationManager.navigate(NavigationEvent.NavigateToHomeAfterTeamCreation)
            }
        )
    }
}

/**
 * Adds the team navigation graph to the NavGraphBuilder.
 * Sets up routes for team and create task screens.
 *
 * @param navigationManager The navigation manager used for navigation events
 */
fun NavGraphBuilder.teamGraph(navigationManager: NavigationManager) {
    composable(
        route = Constants.Navigation.Routes.TEAM,
        arguments = listOf(
            navArgument(Constants.Navigation.Params.TEAM_ID) { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val teamId = backStackEntry.arguments?.getString(Constants.Navigation.Params.TEAM_ID)
        requireNotNull(teamId) { "teamId parameter wasn't found" }
        TeamScreen(
            teamId = teamId,
            onNavigateToTask = { teamId, taskId ->
                navigationManager.navigate(NavigationEvent.NavigateToTask(teamId, taskId))
            }
        )
    }

    composable(
        route = Constants.Navigation.Routes.CREATE_TASK,
        arguments = listOf(navArgument(Constants.Navigation.Params.TEAM_ID) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        val teamId = backStackEntry.arguments?.getString(Constants.Navigation.Params.TEAM_ID)
        requireNotNull(teamId) { "teamId parameter wasn't found" }
        CreateTaskScreen(
            afterTaskCreated = {
                navigationManager.navigate(
                    NavigationEvent.NavigateToTeamAfterTaskCreation(
                        teamId
                    )
                )
            }
        )
    }
}

/**
 * Adds the profile navigation graph to the NavGraphBuilder.
 * Sets up the route for the profile screen.
 *
 * @param navigationManager The navigation manager used for navigation events
 */
fun NavGraphBuilder.profileGraph(navigationManager: NavigationManager) {
    composable(
        route = Screen.buildProfileRoute()
    ) {
        ProfileScreen(userId = Constants.User.USER_ID)
    }
}

/**
 * Adds the task navigation graph to the NavGraphBuilder.
 * Sets up the route for the task screen.
 *
 * @param navigationManager The navigation manager used for navigation events
 */
fun NavGraphBuilder.taskGraph(navigationManager: NavigationManager) {
    composable(
        route = Constants.Navigation.Routes.TASK,
        arguments = listOf(
            navArgument(Constants.Navigation.Params.TEAM_ID) { type = NavType.StringType },
            navArgument(Constants.Navigation.Params.TASK_ID) { type = NavType.StringType }
        )
    ) { navBackStackEntry ->
        val teamId =
            navBackStackEntry.arguments?.getString(Constants.Navigation.Params.TEAM_ID) ?: ""
        val taskId =
            navBackStackEntry.arguments?.getString(Constants.Navigation.Params.TASK_ID) ?: ""
        TaskScreen(
            teamId = teamId,
            taskId = taskId
        )
    }
}