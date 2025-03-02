package it.polito.thesisapp.viewmodel

import androidx.lifecycle.viewModelScope
import it.polito.thesisapp.model.Profile
import it.polito.thesisapp.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing profile data.
 *
 * @property profileRepository Repository for profile-related operations.
 */
class ProfileViewModel(
    private val profileRepository: ProfileRepository = ProfileRepository()
) : BaseViewModel() {

    /**
     * StateFlow to hold the current profile.
     */
    private val _profile = MutableStateFlow<Profile?>(null)
    val profile = _profile

    /**
     * StateFlow to indicate if a loading operation is in progress.
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    /**
     * Loads the profile for the specified user ID.
     *
     * @param userId The ID of the user whose profile is to be loaded.
     */
    fun loadProfile(userId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                profileRepository.getProfileFlow(userId).collect { profile ->
                    _profile.value = profile
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                setError(e.message)
                _isLoading.value = false
            }
        }
    }
}