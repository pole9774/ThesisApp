package it.polito.thesisapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import it.polito.thesisapp.model.Task
import it.polito.thesisapp.model.TaskStatus
import it.polito.thesisapp.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing tasks.
 *
 * @property teamRepository The repository for managing team data.
 */
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val teamRepository: TeamRepository
) : ViewModel() {

    // StateFlow to hold the task data
    private val _task = MutableStateFlow<Task?>(null)
    val task = _task

    // StateFlow to track loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    /**
     * Loads the task data for the specified team and task IDs.
     *
     * @param teamId The ID of the team.
     * @param taskId The ID of the task.
     */
    fun loadTask(teamId: String, taskId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val teamRef = FirebaseFirestore.getInstance().collection("teams").document(teamId)
            teamRepository.getTeamFlow(teamRef).collect { team ->
                val foundTask = team?.tasks?.find { it.id == taskId }
                _task.value = foundTask
                _isLoading.value = false
            }
        }
    }

    /**
     * Updates the status of the specified task.
     *
     * @param teamId The ID of the team.
     * @param taskId The ID of the task.
     * @param newStatus The new status of the task.
     */
    fun updateTaskStatus(teamId: String, taskId: String, newStatus: TaskStatus) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                teamRepository.updateTaskStatus(teamId, taskId, newStatus)
                // Task is automatically updated through the flow
            } catch (e: Exception) {
                e.message?.let { Log.e("ERROR", it) }
            } finally {
                _isLoading.value = false
            }
        }
    }
}