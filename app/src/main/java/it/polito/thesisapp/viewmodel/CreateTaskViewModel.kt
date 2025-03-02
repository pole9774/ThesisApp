package it.polito.thesisapp.viewmodel

import androidx.lifecycle.viewModelScope
import it.polito.thesisapp.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for creating a task.
 *
 * @property repository Repository for team-related operations.
 */
class CreateTaskViewModel(
    private val repository: TeamRepository = TeamRepository()
) : BaseViewModel() {

    /**
     * StateFlow to indicate if the task has been created.
     */
    private val _taskCreated = MutableStateFlow(false)
    val taskCreated: StateFlow<Boolean> = _taskCreated

    /**
     * StateFlow to hold the team ID.
     */
    private val _teamId = MutableStateFlow<String?>(null)
    val teamId: StateFlow<String?> = _teamId

    /**
     * Creates a task with the given details.
     *
     * @param teamId The ID of the team.
     * @param taskName The name of the task.
     * @param taskDescription The description of the task.
     */
    fun createTask(teamId: String, taskName: String, taskDescription: String) {
        if (!validateNotBlank(taskName, "Task name")) {
            return
        }

        viewModelScope.launch {
            try {
                repository.createTask(teamId, taskName, taskDescription)
                _taskCreated.value = true
                setError(null)
            } catch (e: Exception) {
                _taskCreated.value = false
                setError(e.message)
                println("Error in ViewModel: ${e.message}")
            }
        }
    }
}