package it.polito.thesisapp.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.thesisapp.model.Team
import it.polito.thesisapp.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TeamViewModel(
    private val teamRepository: TeamRepository = TeamRepository()
) : BaseViewModel() {

    private val _team = MutableStateFlow<Team?>(null)
    val team: StateFlow<Team?> = _team

    fun loadTeam(teamId: String) {
        viewModelScope.launch {
            val teamRef = FirebaseFirestore.getInstance().collection("teams").document(teamId)
            teamRepository.getTeamFlow(teamRef).collect { team ->
                _team.value = team
            }
        }
    }
}