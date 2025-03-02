package it.polito.thesisapp.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.thesisapp.model.Team
import it.polito.thesisapp.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
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