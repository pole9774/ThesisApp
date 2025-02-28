package it.polito.thesisapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import it.polito.thesisapp.ui.screens.CreateTaskScreen
import it.polito.thesisapp.ui.screens.CreateTeamScreen
import it.polito.thesisapp.ui.screens.HomeScreen
import it.polito.thesisapp.ui.screens.TeamScreen
import it.polito.thesisapp.utils.Constants
import it.polito.thesisapp.navigation.NavigationManager.NavigationEvent

fun NavGraphBuilder.homeGraph(
    navigationManager: NavigationManager,
) {
    composable(Screen.buildHomeRoute()) {
        HomeScreen(
            userId = Constants.User.USER_ID,
            onNavigateToTeam = { teamId ->
                navigationManager.navigate(NavigationEvent.NavigateToTeam(teamId))
            }
        )
    }
    composable(Screen.buildCreateTeamRoute()) {
        CreateTeamScreen(
            navigationManager = navigationManager,
            afterTeamCreated = {
                navigationManager.navigate(NavigationEvent.NavigateToHomeAfterTeamCreation)
            }
        )
    }
}

fun NavGraphBuilder.teamGraph(navigationManager: NavigationManager) {
    composable(
        route = Constants.Navigation.Routes.TEAM,
        arguments = listOf(
            navArgument(Constants.Navigation.Params.TEAM_ID) { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val teamId = backStackEntry.arguments?.getString(Constants.Navigation.Params.TEAM_ID)
        requireNotNull(teamId) { "teamId parameter wasn't found" }
        TeamScreen(teamId = teamId)
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
            teamId = teamId,
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