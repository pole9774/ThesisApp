package it.polito.thesisapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.polito.thesisapp.navigation.NavigationManager
import it.polito.thesisapp.navigation.NavigationManager.NavigationEvent
import it.polito.thesisapp.navigation.Screen
import it.polito.thesisapp.utils.Constants
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the main screen that handles floating action button logic.
 * Centralizes navigation and action handling for the FAB across different screens.
 */
@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {
    /**
     * Handles floating action button clicks based on current route
     *
     * @param navigationManager Navigation manager for handling navigation events
     * @param currentRoute Current navigation route
     * @param createTeamAction Function to create a team with name and description
     * @param createTaskAction Function to create a task with team ID, name and description
     */
    fun handleFabAction(
        navigationManager: NavigationManager,
        currentRoute: String?,
        createTeamAction: (String, String, () -> Unit) -> Unit,
        createTaskAction: (String, String, String, () -> Unit) -> Unit
    ) {
        viewModelScope.launch {
            try {
                when {
                    Screen.isHomeRoute(currentRoute) -> {
                        navigationManager.navigate(NavigationEvent.NavigateToCreateTeam)
                    }

                    Screen.isTeamRoute(currentRoute) -> {
                        val teamId = navigationManager.getArgument(Constants.Navigation.Params.TEAM_ID)
                        if (teamId != null) {
                            navigationManager.navigate(NavigationEvent.NavigateToCreateTask(teamId))
                        }
                    }

                    Screen.isCreateTeamRoute(currentRoute) -> {
                        val teamName = navigationManager.getCurrentArgument<String>(Constants.Navigation.Tags.TEAM_NAME)
                        val teamDescription = navigationManager.getCurrentArgument<String>(Constants.Navigation.Tags.TEAM_DESCRIPTION) ?: ""
                        if (!teamName.isNullOrBlank()) {
                            createTeamAction(teamName, teamDescription) {
                                navigationManager.navigate(NavigationEvent.NavigateToHomeAfterTeamCreation)
                            }
                        }
                    }

                    Screen.isCreateTaskRoute(currentRoute) -> {
                        val teamId = navigationManager.getArgument(Constants.Navigation.Params.TEAM_ID)
                        val taskName = navigationManager.getCurrentArgument<String>(Constants.Navigation.Tags.TASK_NAME)
                        val taskDescription = navigationManager.getCurrentArgument<String>(Constants.Navigation.Tags.TASK_DESCRIPTION) ?: ""

                        if (!taskName.isNullOrBlank() && teamId != null) {
                            createTaskAction(teamId, taskName, taskDescription) {
                                navigationManager.navigate(NavigationEvent.NavigateToTeamAfterTaskCreation(teamId))
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Error", it) }
            }
        }
    }
}