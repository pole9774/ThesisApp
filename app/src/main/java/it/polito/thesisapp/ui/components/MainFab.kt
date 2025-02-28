package it.polito.thesisapp.ui.components

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import it.polito.thesisapp.navigation.NavigationManager
import it.polito.thesisapp.navigation.Screen
import it.polito.thesisapp.ui.LocalNavController
import it.polito.thesisapp.utils.Constants
import it.polito.thesisapp.viewmodel.CreateTaskViewModel
import it.polito.thesisapp.viewmodel.CreateTeamViewModel

@Composable
fun MainFab(
    currentRoute: String?,
    onFabClick: () -> Unit
) {
    val navController = LocalNavController.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // Get the base route without parameters
    val baseRoute = navBackStackEntry?.destination?.route?.substringBefore("/{")

    baseRoute == "team" && navBackStackEntry?.arguments?.getString(Constants.FirestoreFields.Team.TEAM_ID) != null

    if (Screen.isHomeRoute(currentRoute) ||
        Screen.isTeamRoute(currentRoute) ||
        Screen.isCreateTeamRoute(currentRoute) ||
        Screen.isCreateTaskRoute(currentRoute)
    ) {
        FloatingActionButton(
            onClick = onFabClick,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            val icon =
                if (Screen.isCreateTeamRoute(currentRoute) || Screen.isCreateTaskRoute(currentRoute)) {
                    Icons.Default.Check
                } else {
                    Icons.Default.Add
                }

            Icon(
                imageVector = icon,
                contentDescription = getFabDescription(currentRoute)
            )
        }
    }
}

private fun getFabDescription(currentRoute: String?): String =
    when {
        Screen.isHomeRoute(currentRoute) -> "Create Team"
        Screen.isTeamRoute(currentRoute) -> "Create Task"
        Screen.isCreateTeamRoute(currentRoute) -> "Save Team"
        Screen.isCreateTaskRoute(currentRoute) -> "Save Task"
        else -> "Add"
    }


fun handleFabClick(
    navigationManager: NavigationManager,
    currentRoute: String?,
    createTeamViewModel: CreateTeamViewModel,
    createTaskViewModel: CreateTaskViewModel
) {
    when {
        Screen.isHomeRoute(currentRoute) -> {
            navigationManager.navigateToCreateTeam()
        }

        Screen.isTeamRoute(currentRoute) -> {
            val teamId = navigationManager.getArgument(Constants.FirestoreFields.Team.TEAM_ID)
            Log.d("AAA", "teamId in handleFabClick - 2nd branch: $teamId")
            if (teamId != null) {
                navigationManager.navigateToCreateTask(teamId)
            }
        }

        Screen.isCreateTeamRoute(currentRoute) -> {
            val teamName = navigationManager.getCurrentArgument<String>(Constants.Tags.TEAM_NAME)
            if (!teamName.isNullOrBlank()) {
                createTeamViewModel.submitTeamName(teamName)
            }
        }

        Screen.isCreateTaskRoute(currentRoute) -> {
            val teamId = navigationManager.getArgument(Constants.FirestoreFields.Team.TEAM_ID)
            Log.d("AAA", "teamId in handleFabClick - 4th branch: $teamId")

            val taskName = navigationManager.getCurrentArgument<String>(Constants.Tags.TASK_NAME)
            val taskDescription =
                navigationManager.getCurrentArgument<String>(Constants.Tags.TASK_DESCRIPTION) ?: ""

            if (!taskName.isNullOrBlank() && teamId != null) {
                createTaskViewModel.createTask(teamId, taskName, taskDescription)
                navigationManager.navigateToTeamAfterTaskCreation(teamId)
            }
        }
    }
}