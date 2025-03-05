package it.polito.thesisapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.polito.thesisapp.model.Profile
import it.polito.thesisapp.repository.ProfileRepository
import it.polito.thesisapp.repository.TeamRepository
import it.polito.thesisapp.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for creating a team.
 *
 * @property teamRepository Repository for team-related operations.
 * @property profileRepository Repository for profile-related operations.
 */
@HiltViewModel
class CreateTeamViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

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

    fun submitTeamAndNavigate(
        teamName: String,
        teamDescription: String,
        onSuccess: () -> Unit
    ) {
        if (teamName.isBlank()) {
            return
        }

        viewModelScope.launch {
            try {
                teamRepository.createTeam(teamName, teamDescription, _selectedProfileIds.value)
                _teamCreated.value = true
                onSuccess()
            } catch (e: Exception) {
                _teamCreated.value = false
            }
        }
    }
}