package it.polito.thesisapp.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Provides ViewModels for the application components.
 * Centralizes ViewModel creation to ensure consistency.
 */
object AppViewModelProvider {
    @Composable
    fun createTeamViewModel(): CreateTeamViewModel = viewModel()

    @Composable
    fun homeViewModel(): HomeViewModel = viewModel()

    @Composable
    fun teamViewModel(): TeamViewModel = viewModel()
}