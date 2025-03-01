package it.polito.thesisapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import it.polito.thesisapp.navigation.NavigationManager
import it.polito.thesisapp.navigation.NavigationManager.NavigationEvent
import it.polito.thesisapp.navigation.Screen
import it.polito.thesisapp.utils.Constants
import it.polito.thesisapp.viewmodel.AppViewModelProvider

@Composable
fun MainFab(
    currentRoute: String?,
    onFabClick: () -> Unit
) {
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
    currentRoute: String?
) {
    when {
        Screen.isHomeRoute(currentRoute) -> {
            navigationManager.navigate(NavigationEvent.NavigateToCreateTeam)
        }

        Screen.isTeamRoute(currentRoute) -> {
            val teamId = navigationManager.getArgument(Constants.Navigation.Params.TEAM_ID)
            if (teamId != null) {
                navigationManager.navigate(
                    NavigationEvent.NavigateToCreateTask(
                        teamId
                    )
                )
            }
        }

        Screen.isCreateTeamRoute(currentRoute) -> {
            val teamName =
                navigationManager.getCurrentArgument<String>(Constants.Navigation.Tags.TEAM_NAME)
            val teamDescription =
                navigationManager.getCurrentArgument<String>(Constants.Navigation.Tags.TEAM_DESCRIPTION)
                    ?: ""
            if (!teamName.isNullOrBlank()) {
                val viewModel = AppViewModelProvider.getCreateTeamViewModel(
                    navigationManager.getCurrentBackStackEntry() ?: return,
                    navigationManager.getCurrentBackStackEntry()?.defaultViewModelProviderFactory
                        ?: return
                )
                viewModel.submitTeam(teamName, teamDescription)
            }
        }

        Screen.isCreateTaskRoute(currentRoute) -> {
            val teamId = navigationManager.getArgument(Constants.Navigation.Params.TEAM_ID)
            val taskName =
                navigationManager.getCurrentArgument<String>(Constants.Navigation.Tags.TASK_NAME)
            val taskDescription =
                navigationManager.getCurrentArgument<String>(Constants.Navigation.Tags.TASK_DESCRIPTION)
                    ?: ""

            if (!taskName.isNullOrBlank() && teamId != null) {
                val viewModel = AppViewModelProvider.getCreateTaskViewModel(
                    navigationManager.getCurrentBackStackEntry() ?: return,
                    navigationManager.getCurrentBackStackEntry()?.defaultViewModelProviderFactory
                        ?: return
                )
                viewModel.createTask(teamId, taskName, taskDescription)
            }
        }
    }
}