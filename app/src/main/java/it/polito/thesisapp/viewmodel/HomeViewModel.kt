package it.polito.thesisapp.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import it.polito.thesisapp.model.Profile
import it.polito.thesisapp.model.Team
import it.polito.thesisapp.repository.ProfileRepository
import it.polito.thesisapp.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val profileRepository: ProfileRepository = ProfileRepository(),
    private val teamRepository: TeamRepository = TeamRepository()
) : BaseViewModel() {

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile

    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    fun loadProfile(userId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                profileRepository.getProfileFlow(userId).collect { profile ->
                    _profile.value = profile
                    loadTeams(profile?.teams ?: emptyList())
                }
            } catch (e: Exception) {
                setError(e.message)
                _isLoading.value = false
            }
        }
    }

    private fun loadTeams(teamRefs: List<DocumentReference>) {
        viewModelScope.launch {
            try {
                _teams.value = emptyList()
                val teamsMap = mutableMapOf<String, Team>()

                if (teamRefs.isEmpty()) {
                    _isLoading.value = false
                    return@launch
                }

                teamRefs.forEach { ref ->
                    viewModelScope.launch {
                        teamRepository.getTeamFlow(ref).collect { team ->
                            if (team != null) {
                                teamsMap[team.id] = team
                            } else {
                                teamsMap.remove(ref.id)
                            }
                            _teams.value = teamsMap.values.sortedBy { it.name }
                            _isLoading.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                setError(e.message)
                _isLoading.value = false
            }
        }
    }
}