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

    /**
     * Creates an instance of CreateTeamViewModel.
     *
     * @return An instance of CreateTeamViewModel.
     */
    @Composable
    fun createTeamViewModel(): CreateTeamViewModel = viewModel {
        CreateTeamViewModel(teamRepository)
    }

    /**
     * Creates an instance of HomeViewModel.
     *
     * @return An instance of HomeViewModel.
     */
    @Composable
    fun homeViewModel(): HomeViewModel = viewModel {
        HomeViewModel(profileRepository, teamRepository)
    }

    /**
     * Creates an instance of TeamViewModel.
     *
     * @return An instance of TeamViewModel.
     */
    @Composable
    fun teamViewModel(): TeamViewModel = viewModel {
        TeamViewModel(teamRepository)
    }

    /**
     * Creates an instance of CreateTaskViewModel.
     *
     * @return An instance of CreateTaskViewModel.
     */
    @Composable
    fun createTaskViewModel(): CreateTaskViewModel = viewModel {
        CreateTaskViewModel(teamRepository)
    }

    /**
     * Creates an instance of ProfileViewModel.
     *
     * @return An instance of ProfileViewModel.
     */
    @Composable
    fun profileViewModel(): ProfileViewModel = viewModel {
        ProfileViewModel(profileRepository)
    }

    /**
     * Retrieves an instance of TeamViewModel for non-composable contexts.
     *
     * @param viewModelStoreOwner The owner of the ViewModelStore.
     * @param factory The factory to create the ViewModel.
     * @return An instance of TeamViewModel.
     */
    fun getTeamViewModel(
        viewModelStoreOwner: ViewModelStoreOwner,
        factory: ViewModelProvider.Factory
    ): TeamViewModel {
        return ViewModelProvider(viewModelStoreOwner, factory)[TeamViewModel::class.java]
    }

    /**
     * Retrieves an instance of CreateTeamViewModel for non-composable contexts.
     *
     * @param viewModelStoreOwner The owner of the ViewModelStore.
     * @param factory The factory to create the ViewModel.
     * @return An instance of CreateTeamViewModel.
     */
    fun getCreateTeamViewModel(
        viewModelStoreOwner: ViewModelStoreOwner,
        factory: ViewModelProvider.Factory
    ): CreateTeamViewModel {
        return ViewModelProvider(viewModelStoreOwner, factory)[CreateTeamViewModel::class.java]
    }

    /**
     * Retrieves an instance of HomeViewModel for non-composable contexts.
     *
     * @param viewModelStoreOwner The owner of the ViewModelStore.
     * @param factory The factory to create the ViewModel.
     * @return An instance of HomeViewModel.
     */
    fun getHomeViewModel(
        viewModelStoreOwner: ViewModelStoreOwner,
        factory: ViewModelProvider.Factory
    ): HomeViewModel {
        return ViewModelProvider(viewModelStoreOwner, factory)[HomeViewModel::class.java]
    }

    /**
     * Retrieves an instance of CreateTaskViewModel for non-composable contexts.
     *
     * @param viewModelStoreOwner The owner of the ViewModelStore.
     * @param factory The factory to create the ViewModel.
     * @return An instance of CreateTaskViewModel.
     */
    fun getCreateTaskViewModel(
        viewModelStoreOwner: ViewModelStoreOwner,
        factory: ViewModelProvider.Factory
    ): CreateTaskViewModel {
        return ViewModelProvider(viewModelStoreOwner, factory)[CreateTaskViewModel::class.java]
    }

    /**
     * Retrieves an instance of ProfileViewModel for non-composable contexts.
     *
     * @param viewModelStoreOwner The owner of the ViewModelStore.
     * @param factory The factory to create the ViewModel.
     * @return An instance of ProfileViewModel.
     */
    fun getProfileViewModel(
        viewModelStoreOwner: ViewModelStoreOwner,
        factory: ViewModelProvider.Factory
    ): ProfileViewModel {
        return ViewModelProvider(viewModelStoreOwner, factory)[ProfileViewModel::class.java]
    }
}