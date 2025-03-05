package it.polito.thesisapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import dagger.hilt.android.lifecycle.HiltViewModel
import it.polito.thesisapp.model.Profile
import it.polito.thesisapp.model.Task
import it.polito.thesisapp.model.Team
import it.polito.thesisapp.repository.ProfileRepository
import it.polito.thesisapp.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home screen.
 *
 * @property profileRepository Repository for profile-related operations.
 * @property teamRepository Repository for team-related operations.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val teamRepository: TeamRepository
) : ViewModel() {

    /**
     * StateFlow to hold the current profile.
     */
    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile

    /**
     * StateFlow to hold the list of teams.
     */
    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams

    /**
     * StateFlow to indicate if a loading operation is in progress.
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    /**
     * Enum class to define task sorting modes.
     */
    enum class TaskSortMode { DATE_DESC, NAME_ASC, NAME_DESC }

    /**
     * StateFlow to hold the current task sorting mode.
     */
    private val _taskSortMode = MutableStateFlow(TaskSortMode.DATE_DESC)
    val taskSortMode = _taskSortMode

    /**
     * StateFlow to hold the sorted list of tasks.
     */
    private val _sortedTasks = MutableStateFlow<List<Task>>(emptyList())
    val sortedTasks = _sortedTasks

    /**
     * StateFlow to hold the index of the selected team.
     */
    private val _selectedTeamIndex = MutableStateFlow(0)
    val selectedTeamIndex = _selectedTeamIndex

    /**
     * Initializes the ViewModel and sets up task sorting.
     */
    init {
        viewModelScope.launch {
            combine(_teams, _taskSortMode, _selectedTeamIndex) { teams, sortMode, selectedIndex ->
                val currentTeam = teams.getOrNull(selectedIndex)
                sortTasks(currentTeam?.tasks ?: emptyList(), sortMode)
            }.collect { sortedList ->
                _sortedTasks.value = sortedList
            }
        }
    }

    /**
     * Sorts tasks based on the specified sorting mode.
     *
     * @param tasks The list of tasks to sort.
     * @param mode The sorting mode to use.
     * @return The sorted list of tasks.
     */
    private fun sortTasks(tasks: List<Task>, mode: TaskSortMode): List<Task> {
        return when (mode) {
            TaskSortMode.DATE_DESC -> tasks.sortedByDescending { it.creationDate }
            TaskSortMode.NAME_ASC -> tasks.sortedBy { it.name }
            TaskSortMode.NAME_DESC -> tasks.sortedByDescending { it.name }
        }
    }

    /**
     * Toggles the task sorting mode.
     */
    fun toggleSortMode() {
        _taskSortMode.value = when (_taskSortMode.value) {
            TaskSortMode.DATE_DESC -> TaskSortMode.NAME_ASC
            TaskSortMode.NAME_ASC -> TaskSortMode.NAME_DESC
            TaskSortMode.NAME_DESC -> TaskSortMode.DATE_DESC
        }
    }

    /**
     * Loads the profile for the specified user ID.
     *
     * @param userId The ID of the user whose profile is to be loaded.
     */
    fun loadProfile(userId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                profileRepository.getProfileFlow(userId).collect { profile ->
                    _profile.value = profile
                    loadTeams(profile?.teams ?: emptyList())
                }
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    /**
     * Loads the teams for the specified list of team references.
     *
     * @param teamRefs The list of team document references.
     */
    private fun loadTeams(teamRefs: List<DocumentReference>) {
        viewModelScope.launch {
            try {
                _teams.value = emptyList()
                val teamsMap = mutableMapOf<String, Team>()

                if (teamRefs.isEmpty()) {
                    _isLoading.value = false
                    return@launch
                }

                teamRefs.forEach { ref ->
                    viewModelScope.launch {
                        teamRepository.getTeamFlow(ref).collect { team ->
                            if (team != null) {
                                teamsMap[team.id] = team
                            } else {
                                teamsMap.remove(ref.id)
                            }
                            _teams.value = teamsMap.values.sortedBy { it.name }
                            _isLoading.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    /**
     * Selects a team by its index.
     *
     * @param index The index of the team to select.
     */
    fun selectTeam(index: Int) {
        if (index >= 0 && index < _teams.value.size) {
            _selectedTeamIndex.value = index
        }
    }
}