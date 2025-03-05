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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val teamRepository: TeamRepository
) : ViewModel() {

    private val _task = MutableStateFlow<Task?>(null)
    val task: StateFlow<Task?> = _task

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

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