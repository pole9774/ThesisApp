package it.polito.thesisapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.polito.thesisapp.model.Profile
import it.polito.thesisapp.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing profile data.
 *
 * @property profileRepository Repository for profile-related operations.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

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
                _isLoading.value = false
            }
        }
    }
}