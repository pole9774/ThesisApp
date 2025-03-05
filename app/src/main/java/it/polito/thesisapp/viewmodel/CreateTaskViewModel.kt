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
 * ViewModel for creating tasks.
 *
 * @property teamRepository The repository for managing team data.
 */
@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val teamRepository: TeamRepository
) : ViewModel() {

    // StateFlow to track if a task has been created
    private val _taskCreated = MutableStateFlow(false)
    val taskCreated: StateFlow<Boolean> = _taskCreated

    // StateFlow to hold the team ID
    private val _teamId = MutableStateFlow<String?>(null)
    val teamId: StateFlow<String?> = _teamId

    /**
     * Submits a task and navigates to the next screen on success.
     *
     * @param teamId The ID of the team.
     * @param taskName The name of the task.
     * @param taskDescription The description of the task.
     * @param onSuccess Callback function to be called on successful task creation.
     */
    fun submitTaskAndNavigate(
        teamId: String,
        taskName: String,
        taskDescription: String,
        onSuccess: () -> Unit
    ) {
        if (taskName.isBlank()) {
            return
        }

        viewModelScope.launch {
            try {
                teamRepository.createTask(teamId, taskName, taskDescription)
                _taskCreated.value = true
                onSuccess()
            } catch (e: Exception) {
                _taskCreated.value = false
            }
        }
    }
}