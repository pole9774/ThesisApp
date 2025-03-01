package it.polito.thesisapp.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import it.polito.thesisapp.model.Profile
import it.polito.thesisapp.model.Task
import it.polito.thesisapp.model.Team
import it.polito.thesisapp.repository.ProfileRepository
import it.polito.thesisapp.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(
    private val profileRepository: ProfileRepository = ProfileRepository(),
    private val teamRepository: TeamRepository = TeamRepository()
) : BaseViewModel() {

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile

    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    enum class TaskSortMode { DATE_DESC, NAME_ASC, NAME_DESC }

    private val _taskSortMode = MutableStateFlow(TaskSortMode.DATE_DESC)
    val taskSortMode = _taskSortMode

    private val _sortedTasks = MutableStateFlow<List<Task>>(emptyList())
    val sortedTasks = _sortedTasks

    private val _selectedTeamIndex = MutableStateFlow(0)
    val selectedTeamIndex = _selectedTeamIndex

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

    private fun sortTasks(tasks: List<Task>, mode: TaskSortMode): List<Task> {
        return when (mode) {
            TaskSortMode.DATE_DESC -> tasks.sortedByDescending { it.creationDate }
            TaskSortMode.NAME_ASC -> tasks.sortedBy { it.name }
            TaskSortMode.NAME_DESC -> tasks.sortedByDescending { it.name }
        }
    }

    fun toggleSortMode() {
        _taskSortMode.value = when (_taskSortMode.value) {
            TaskSortMode.DATE_DESC -> TaskSortMode.NAME_ASC
            TaskSortMode.NAME_ASC -> TaskSortMode.NAME_DESC
            TaskSortMode.NAME_DESC -> TaskSortMode.DATE_DESC
        }
    }

    fun loadProfile(userId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                profileRepository.getProfileFlow(userId).collect { profile ->
                    _profile.value = profile
                    loadTeams(profile?.teams ?: emptyList())
                }
            } catch (e: Exception) {
                setError(e.message)
                _isLoading.value = false
            }
        }
    }

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
                setError(e.message)
                _isLoading.value = false
            }
        }
    }

    fun selectTeam(index: Int) {
        if (index >= 0 && index < _teams.value.size) {
            _selectedTeamIndex.value = index
        }
    }
}