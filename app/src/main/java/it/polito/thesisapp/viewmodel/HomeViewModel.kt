package it.polito.thesisapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import it.polito.thesisapp.model.Profile
import it.polito.thesisapp.model.Team
import it.polito.thesisapp.repository.ProfileRepository
import it.polito.thesisapp.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Home screen.
 *
 * @property profileRepository The repository used to fetch profile data.
 * @property teamRepository The repository used to fetch team data.
 */
class HomeViewModel(
    private val profileRepository: ProfileRepository = ProfileRepository(),
    private val teamRepository: TeamRepository = TeamRepository()
) : ViewModel() {

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile

    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams

    /**
     * Loads the profile data for the given user ID and updates the state flows.
     *
     * @param userId The unique identifier of the user.
     */
    fun loadProfile(userId: String) {
        viewModelScope.launch {
            profileRepository.getProfileFlow(userId).collect { profile ->
                _profile.value = profile
                loadTeams(profile?.teams ?: emptyList())
            }
        }
    }

    /**
     * Loads the team data for the given list of team references and updates the state flows.
     *
     * @param teamRefs The list of team document references.
     */
    private fun loadTeams(teamRefs: List<DocumentReference>) {
        viewModelScope.launch {
            _teams.value = emptyList()
            val teamsMap = mutableMapOf<String, Team>()

            teamRefs.forEach { ref ->
                viewModelScope.launch {
                    teamRepository.getTeamFlow(ref).collect { team ->
                        if (team != null) {
                            teamsMap[team.id] = team
                            _teams.value = teamsMap.values.sortedBy { it.name }
                        } else {
                            teamsMap.remove(ref.id)
                            _teams.value = teamsMap.values.sortedBy { it.name }
                        }
                    }
                }
            }
        }
    }
}