package it.polito.thesisapp.viewmodel

import androidx.lifecycle.viewModelScope
import it.polito.thesisapp.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateTaskViewModel(
    private val repository: TeamRepository = TeamRepository()
) : BaseViewModel() {

    private val _taskCreated = MutableStateFlow(false)
    val taskCreated: StateFlow<Boolean> = _taskCreated

    private val _teamId = MutableStateFlow<String?>(null)
    val teamId: StateFlow<String?> = _teamId

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

    fun submitTaskDetails(teamId: String, taskName: String, taskDescription: String) {
        if (!validateNotBlank(taskName, "Task name")) {
            return
        }

        createTask(teamId, taskName, taskDescription)
    }
}