package it.polito.thesisapp.viewmodel

import androidx.lifecycle.viewModelScope
import it.polito.thesisapp.model.Profile
import it.polito.thesisapp.repository.ProfileRepository
import it.polito.thesisapp.repository.TeamRepository
import it.polito.thesisapp.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for creating a team.
 *
 * @property teamRepository Repository for team-related operations.
 * @property profileRepository Repository for profile-related operations.
 */
class CreateTeamViewModel(
    private val teamRepository: TeamRepository = TeamRepository(),
    private val profileRepository: ProfileRepository = ProfileRepository()
) : BaseViewModel() {

    /**
     * StateFlow to indicate if the team has been created.
     */
    private val _teamCreated = MutableStateFlow(false)
    val teamCreated: StateFlow<Boolean> = _teamCreated

    /**
     * StateFlow to hold all profiles.
     */
    private val _allProfiles = MutableStateFlow<List<Profile>>(emptyList())
    val allProfiles = _allProfiles

    /**
     * StateFlow to hold selected profile IDs.
     */
    private val _selectedProfileIds = MutableStateFlow<Set<String>>(setOf())
    val selectedProfileIds = _selectedProfileIds

    /**
     * StateFlow to indicate if a loading operation is in progress.
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    /**
     * Loads all profiles from the repository.
     */
    fun loadAllProfiles() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                profileRepository.getAllProfilesFlow().collect { profiles ->
                    _allProfiles.value = profiles
                    if (_selectedProfileIds.value.isEmpty()) {
                        _selectedProfileIds.value = setOf(Constants.User.USER_ID)
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                setError(e.message)
                _isLoading.value = false
            }
        }
    }

    /**
     * Toggles the selection of a profile.
     *
     * @param profileId The ID of the profile to toggle.
     * @param isSelected Whether the profile is selected.
     */
    fun toggleProfileSelection(profileId: String, isSelected: Boolean) {
        _selectedProfileIds.value = if (isSelected) {
            _selectedProfileIds.value + profileId
        } else {
            _selectedProfileIds.value - profileId
        }
    }

    /**
     * Creates a team with the given name and description.
     *
     * @param teamName The name of the team.
     * @param teamDescription The description of the team.
     */
    private fun createTeam(teamName: String, teamDescription: String) {
        viewModelScope.launch {
            try {
                teamRepository.createTeam(teamName, teamDescription, _selectedProfileIds.value)
                _teamCreated.value = true
                setError(null)
            } catch (e: Exception) {
                _teamCreated.value = false
                setError(e.message)
                println("Error in ViewModel: ${e.message}")
            }
        }
    }

    /**
     * Submits the team creation request.
     *
     * @param teamName The name of the team.
     * @param teamDescription The description of the team.
     */
    fun submitTeam(teamName: String, teamDescription: String) {
        if (!validateNotBlank(teamName, "Team name")) {
            return
        }

        createTeam(teamName, teamDescription)
    }
}