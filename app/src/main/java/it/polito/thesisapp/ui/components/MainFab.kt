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
import it.polito.thesisapp.viewmodel.AppViewModelProvider
import it.polito.thesisapp.viewmodel.CreateTaskViewModel
import it.polito.thesisapp.viewmodel.CreateTeamViewModel

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
    val currentBackStackEntry = navigationManager.getCurrentBackStackEntry()
    val viewModelStoreOwner = currentBackStackEntry ?: return

    when {
        Screen.isHomeRoute(currentRoute) -> {
            navigationManager.navigateToCreateTeam()
        }

        Screen.isTeamRoute(currentRoute) -> {
            val teamId = navigationManager.getArgument(Constants.FirestoreFields.Team.TEAM_ID)
            if (teamId != null) {
                navigationManager.navigateToCreateTask(teamId)
            }
        }

        Screen.isCreateTeamRoute(currentRoute) -> {
            val teamName =
                navigationManager.getCurrentArgument<String>(Constants.Navigation.Tags.TEAM_NAME)
            if (!teamName.isNullOrBlank()) {
                val viewModel = AppViewModelProvider.getCreateTeamViewModel(
                    viewModelStoreOwner,
                    viewModelStoreOwner.defaultViewModelProviderFactory
                )
                viewModel.submitTeamName(teamName)
            }
        }

        Screen.isCreateTaskRoute(currentRoute) -> {
            val teamId = navigationManager.getArgument(Constants.FirestoreFields.Team.TEAM_ID)
            val taskName =
                navigationManager.getCurrentArgument<String>(Constants.Navigation.Tags.TASK_NAME)
            val taskDescription =
                navigationManager.getCurrentArgument<String>(Constants.Navigation.Tags.TASK_DESCRIPTION)
                    ?: ""

            if (!taskName.isNullOrBlank() && teamId != null) {
                val viewModel = AppViewModelProvider.getCreateTaskViewModel(
                    viewModelStoreOwner,
                    viewModelStoreOwner.defaultViewModelProviderFactory
                )
                viewModel.createTask(teamId, taskName, taskDescription)
                navigationManager.navigateToTeamAfterTaskCreation(teamId)
            }
        }
    }
}