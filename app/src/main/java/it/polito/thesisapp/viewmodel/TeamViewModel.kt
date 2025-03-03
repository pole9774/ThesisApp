package it.polito.thesisapp.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.thesisapp.model.Task
import it.polito.thesisapp.model.TaskStatus
import it.polito.thesisapp.model.Team
import it.polito.thesisapp.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * ViewModel for managing team data.
 *
 * @property teamRepository Repository for team-related operations.
 */
class TeamViewModel(
    private val teamRepository: TeamRepository = TeamRepository()
) : BaseViewModel() {

    /**
     * StateFlow to hold the current team.
     */
    private val _team = MutableStateFlow<Team?>(null)
    val team = _team

    /**
     * StateFlow to indicate if a loading operation is in progress.
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    enum class TaskSortMode { DATE_DESC, NAME_ASC, NAME_DESC }

    private val _taskSortMode = MutableStateFlow(TaskSortMode.DATE_DESC)
    val taskSortMode = _taskSortMode

    private val _sortedTasks = MutableStateFlow<List<Task>>(emptyList())
    val sortedTasks = _sortedTasks

    private val _selectedStatusFilters = MutableStateFlow(
        TaskStatus.entries.toSet()
    )
    val selectedStatusFilters = _selectedStatusFilters

    init {
        viewModelScope.launch {
            combine(_team, _taskSortMode, _selectedStatusFilters) { team, sortMode, filters ->
                val tasks = team?.tasks ?: emptyList()
                val filteredTasks = tasks.filter { it.status in filters }
                sortTasks(filteredTasks, sortMode)
            }.collect { sortedFilteredList ->
                _sortedTasks.value = sortedFilteredList
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

    fun toggleStatusFilter(status: TaskStatus) {
        val currentFilters = _selectedStatusFilters.value
        _selectedStatusFilters.value = if (status in currentFilters) {
            currentFilters - status
        } else {
            currentFilters + status
        }
    }

    /**
     * Loads the team for the specified team ID.
     *
     * @param teamId The ID of the team to be loaded.
     */
    fun loadTeam(teamId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val teamRef = FirebaseFirestore.getInstance().collection("teams").document(teamId)
            teamRepository.getTeamFlow(teamRef).collect { team ->
                _team.value = team
                _isLoading.value = false
            }
        }
    }
}