package it.polito.thesisapp.viewmodel

import androidx.lifecycle.viewModelScope
import it.polito.thesisapp.model.Profile
import it.polito.thesisapp.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository = ProfileRepository()
) : BaseViewModel() {

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile = _profile

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

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