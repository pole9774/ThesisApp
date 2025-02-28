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
import it.polito.thesisapp.viewmodel.CreateTeamViewModel

fun NavGraphBuilder.homeGraph(
    navigationManager: NavigationManager,
    createTeamViewModel: CreateTeamViewModel
) {
    composable(Screen.buildHomeRoute()) {
        HomeScreen(
            userId = Constants.User.USER_ID,
            onNavigateToTeam = { teamId -> navigationManager.navigateToTeam(teamId) }
        )
    }
    composable(Screen.buildCreateTeamRoute()) {
        CreateTeamScreen(
            navigationManager = navigationManager,
            viewModel = createTeamViewModel,
            afterTeamCreated = { navigationManager.navigateToHomeAfterTeamCreation() }
        )
    }
}

fun NavGraphBuilder.teamGraph(navigationManager: NavigationManager) {
    composable(
        route = "team/{${Constants.FirestoreFields.Team.TEAM_ID}}",
        arguments = listOf(
            navArgument(Constants.FirestoreFields.Team.TEAM_ID) { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val teamId = backStackEntry.arguments?.getString(Constants.FirestoreFields.Team.TEAM_ID)
        requireNotNull(teamId) { "teamId parameter wasn't found" }
        TeamScreen(teamId = teamId)
    }

    composable(
        route = "create_task/{${Constants.FirestoreFields.Team.TEAM_ID}}",
        arguments = listOf(navArgument(Constants.FirestoreFields.Team.TEAM_ID) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        val teamId = backStackEntry.arguments?.getString(Constants.FirestoreFields.Team.TEAM_ID)
        requireNotNull(teamId) { "teamId parameter wasn't found" }
        CreateTaskScreen(
            teamId = teamId,
            afterTaskCreated = { navigationManager.navigateToTeamAfterTaskCreation(teamId) }
        )
    }
}