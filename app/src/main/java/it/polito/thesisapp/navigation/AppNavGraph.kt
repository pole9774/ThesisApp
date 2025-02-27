package it.polito.thesisapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import it.polito.thesisapp.ui.screens.CreateTeamScreen
import it.polito.thesisapp.ui.screens.HomeScreen
import it.polito.thesisapp.ui.screens.TeamScreen
import it.polito.thesisapp.utils.Constants
import it.polito.thesisapp.viewmodel.CreateTeamViewModel

fun NavGraphBuilder.homeGraph(
    navigationManager: NavigationManager,
    createTeamViewModel: CreateTeamViewModel
) {
    composable(Screen.HOME_ROUTE) {
        HomeScreen(
            userId = Constants.User.USER_ID,
            onNavigateToTeam = { teamId -> navigationManager.navigateToTeam(teamId) }
        )
    }
    composable(Screen.CREATE_TEAM_ROUTE) {
        CreateTeamScreen(
            navigationManager = navigationManager,
            viewModel = createTeamViewModel,
            afterTeamCreated = { navigationManager.navigateToHomeAfterTeamCreation() }
        )
    }
}

fun NavGraphBuilder.teamGraph() {
    composable(
        route = Screen.TEAM_ROUTE,
        arguments = listOf(
            navArgument(Constants.FirestoreFields.Team.TEAM_ID) { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val teamId = backStackEntry.arguments?.getString(Constants.FirestoreFields.Team.TEAM_ID)
        requireNotNull(teamId) { "teamId parameter wasn't found" }
        TeamScreen(teamId = teamId)
    }
}