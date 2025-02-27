package it.polito.thesisapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.polito.thesisapp.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateTeamViewModel(
    private val repository: TeamRepository = TeamRepository()
) : ViewModel() {

    private val _teamCreated = MutableStateFlow(false)
    val teamCreated: StateFlow<Boolean> = _teamCreated

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun createTeam(teamName: String) {
        if (teamName.isBlank()) {
            _error.value = "Team name cannot be blank"
            return
        }

        viewModelScope.launch {
            try {
                repository.createTeam(teamName)
                _teamCreated.value = true
                _error.value = null
            } catch (e: Exception) {
                _teamCreated.value = false
                _error.value = e.message
                println("Error in ViewModel: ${e.message}")
            }
        }
    }

    fun submitTeamName(teamName: String) {
        if (teamName.isBlank()) {
            _error.value = "Team name cannot be blank"
            return
        }

        createTeam(teamName)
    }
}