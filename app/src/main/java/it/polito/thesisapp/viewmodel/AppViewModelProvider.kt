package it.polito.thesisapp.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import it.polito.thesisapp.repository.ProfileRepository
import it.polito.thesisapp.repository.TeamRepository

/**
 * Provides ViewModels for the application components.
 * Centralizes ViewModel creation and dependency management.
 *
 * Uses singleton instances of repositories to ensure consistent state
 * management across all ViewModels in the application.
 */
object AppViewModelProvider {
    private val profileRepository by lazy { ProfileRepository() }
    private val teamRepository by lazy { TeamRepository() }

    @Composable
    fun createTeamViewModel(): CreateTeamViewModel = viewModel {
        CreateTeamViewModel(teamRepository)
    }

    @Composable
    fun homeViewModel(): HomeViewModel = viewModel {
        HomeViewModel(profileRepository, teamRepository)
    }

    @Composable
    fun teamViewModel(): TeamViewModel = viewModel {
        TeamViewModel(teamRepository)
    }

    @Composable
    fun createTaskViewModel(): CreateTaskViewModel = viewModel {
        CreateTaskViewModel(teamRepository)
    }

    @Composable
    fun profileViewModel(): ProfileViewModel = viewModel {
        ProfileViewModel(profileRepository)
    }

    // Helper function for non-composable contexts
    fun getTeamViewModel(
        viewModelStoreOwner: ViewModelStoreOwner,
        factory: ViewModelProvider.Factory
    ): TeamViewModel {
        return ViewModelProvider(viewModelStoreOwner, factory)[TeamViewModel::class.java]
    }

    fun getCreateTeamViewModel(
        viewModelStoreOwner: ViewModelStoreOwner,
        factory: ViewModelProvider.Factory
    ): CreateTeamViewModel {
        return ViewModelProvider(viewModelStoreOwner, factory)[CreateTeamViewModel::class.java]
    }

    fun getHomeViewModel(
        viewModelStoreOwner: ViewModelStoreOwner,
        factory: ViewModelProvider.Factory
    ): HomeViewModel {
        return ViewModelProvider(viewModelStoreOwner, factory)[HomeViewModel::class.java]
    }

    fun getCreateTaskViewModel(
        viewModelStoreOwner: ViewModelStoreOwner,
        factory: ViewModelProvider.Factory
    ): CreateTaskViewModel {
        return ViewModelProvider(viewModelStoreOwner, factory)[CreateTaskViewModel::class.java]
    }

    fun getProfileViewModel(
        viewModelStoreOwner: ViewModelStoreOwner,
        factory: ViewModelProvider.Factory
    ): ProfileViewModel {
        return ViewModelProvider(viewModelStoreOwner, factory)[ProfileViewModel::class.java]
    }
}