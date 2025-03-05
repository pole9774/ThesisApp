package it.polito.thesisapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import it.polito.thesisapp.model.Task
import it.polito.thesisapp.model.TaskStatus
import it.polito.thesisapp.model.Team
import it.polito.thesisapp.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing team data and tasks.
 *
 * @property teamRepository The repository for managing team data.
 */
@HiltViewModel
class TeamViewModel @Inject constructor(
    private val teamRepository: TeamRepository
) : ViewModel() {


    // StateFlow to hold the team data
    private val _team = MutableStateFlow<Team?>(null)
    val team = _team

    // StateFlow to track loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    // Enum class for task sort modes
    enum class TaskSortMode { DATE_DESC, NAME_ASC, NAME_DESC }

    // StateFlow to hold the current task sort mode
    private val _taskSortMode = MutableStateFlow(TaskSortMode.DATE_DESC)
    val taskSortMode = _taskSortMode

    // StateFlow to hold the sorted list of tasks
    private val _sortedTasks = MutableStateFlow<List<Task>>(emptyList())
    val sortedTasks = _sortedTasks

    // StateFlow to hold the selected status filters
    private val _selectedStatusFilters = MutableStateFlow(TaskStatus.entries.toSet())
    val selectedStatusFilters = _selectedStatusFilters

    /**
     * Initializes the ViewModel and sets up the task sorting and filtering logic.
     */
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

    /**
     * Sorts the tasks based on the specified sort mode.
     *
     * @param tasks The list of tasks to be sorted.
     * @param mode The sort mode to be applied.
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
     * Toggles the task sort mode.
     */
    fun toggleSortMode() {
        _taskSortMode.value = when (_taskSortMode.value) {
            TaskSortMode.DATE_DESC -> TaskSortMode.NAME_ASC
            TaskSortMode.NAME_ASC -> TaskSortMode.NAME_DESC
            TaskSortMode.NAME_DESC -> TaskSortMode.DATE_DESC
        }
    }

    /**
     * Toggles the status filter for tasks.
     *
     * @param status The status to be toggled.
     */
    fun toggleStatusFilter(status: TaskStatus) {
        val currentFilters = _selectedStatusFilters.value
        _selectedStatusFilters.value = if (status in currentFilters) {
            currentFilters - status
        } else {
            currentFilters + status
        }
    }

    /**
     * Loads the team data for the specified team ID.
     *
     * @param teamId The ID of the team.
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