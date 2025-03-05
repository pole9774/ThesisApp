package it.polito.thesisapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.polito.thesisapp.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for creating a task.
 *
 * @property teamRepository Repository for team-related operations.
 */
@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val teamRepository: TeamRepository
) : ViewModel() {

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
        if (taskName.isNotBlank()) {
            return
        }

        viewModelScope.launch {
            try {
                teamRepository.createTask(teamId, taskName, taskDescription)
                _taskCreated.value = true
            } catch (e: Exception) {
                _taskCreated.value = false
                println("Error in ViewModel: ${e.message}")
            }
        }
    }
}