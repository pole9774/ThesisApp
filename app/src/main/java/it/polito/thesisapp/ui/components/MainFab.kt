package it.polito.thesisapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import it.polito.thesisapp.navigation.NavigationManager
import it.polito.thesisapp.navigation.Screen
import it.polito.thesisapp.utils.Constants
import it.polito.thesisapp.viewmodel.CreateTeamViewModel

@Composable
fun MainFab(
    currentRoute: String?,
    onFabClick: () -> Unit
) {
    if (currentRoute == Screen.HOME_ROUTE || currentRoute == Screen.CREATE_TEAM_ROUTE) {
        FloatingActionButton(
            onClick = onFabClick,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                imageVector = if (currentRoute == Screen.CREATE_TEAM_ROUTE)
                    Icons.Default.Check else Icons.Default.Add,
                contentDescription = getFabDescription(currentRoute)
            )
        }
    }
}

private fun getFabDescription(currentRoute: String?): String =
    when (currentRoute) {
        Screen.HOME_ROUTE -> "Create Team"
        Screen.TEAM_ROUTE -> "Create Task"
        Screen.CREATE_TEAM_ROUTE -> "Save Team"
        else -> "Add"
    }


fun handleFabClick(
    navigationManager: NavigationManager,
    currentRoute: String?,
    createTeamViewModel: CreateTeamViewModel
) {
    when (currentRoute) {
        Screen.HOME_ROUTE -> {
            navigationManager.navigateToCreateTeam()
        }

        Screen.TEAM_ROUTE -> {
            // Create task implementation
        }

        Screen.CREATE_TEAM_ROUTE -> {
            val teamName =
                navigationManager.getCurrentArgument<String>(Constants.Tags.TEAM_NAME) ?: ""

            if (teamName.isNotBlank()) {
                createTeamViewModel.submitTeamName(teamName)
            }
        }
    }
}